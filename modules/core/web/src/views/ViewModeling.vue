<template>
  <DialogInactivityVue ref="dialogInactivity"></DialogInactivityVue>
  <DialogSaveDiagramVue ref="dialogSave"></DialogSaveDiagramVue>
  <DialogSaveToLocalStorageVue ref="dialogSaveToLocal"></DialogSaveToLocalStorageVue>
  <DialogConfirm ref="dialogConfirm" />
  <DialogShowFeedbackVue ref="dialogFeedback"></DialogShowFeedbackVue>
  <DialogAlertVue ref="alertDialog"></DialogAlertVue>
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Diagramm erfolgreich eingereicht </v-snackbar>
  <v-snackbar v-model="snackbarSave" vertical location="right bottom" color="#b03333" :timeout="-1">
    <div class="text-subtitle-1 pb-2">Achtung.. Dein Modell ist noch nicht gespeichert!</div>
    <p>Bitte speichere deine Arbeit, damit keine Änderungen verloren gehen.</p>

    <template #actions>
      <v-btn color="white" variant="text" @click="_openSaveDialog"> Zum Speichern </v-btn>
    </template>
  </v-snackbar>

  <div class="container">
    <v-card v-if="activeTask != null" class="task-floater" elevation="3">
      <v-card-title>Abgabe</v-card-title>
      <v-card-subtitle>
        <span>Aktuelle Aufgabe: {{ activeTask?.name }}</span
        ><br />
        <span>Aus Kurs: {{ activeCourse?.name }}</span
        ><br />
      </v-card-subtitle>
      <v-card-actions>
        <v-row>
          <v-col>
            <v-btn @click="saveDiagram(true)">Zurück zur Abgabe</v-btn>
            <v-btn v-if="!subBtnProgress" @click="triggerfeedback()">Einreichen</v-btn>
            <v-progress-circular v-if="subBtnProgress" indeterminate></v-progress-circular>
          </v-col>
        </v-row>
      </v-card-actions>
      <div class="submissions-results">
        <div class="widget-bar">
          <v-card-title>Feedback</v-card-title>
          <div>
            <v-icon class="widget" icon="mdi-arrow-expand" size="x-small" @click="openFeedback()"></v-icon>
            <v-icon v-if="isCollapsed" class="widget" icon="mdi-arrow-collapse-down" size="x-small" @click="collapseFeedback()"></v-icon>
            <v-icon v-if="!isCollapsed" class="widget" icon="mdi-arrow-expand-up" size="x-small" @click="collapseFeedback()"></v-icon>
          </div>
        </div>

        <TaskSubmissionsResultsTabs v-if="!isCollapsed" ref="taskSubmissionsResultsTabs"></TaskSubmissionsResultsTabs>
      </div>
    </v-card>

    <div class="navigation">
      <v-system-bar color="white" elevation="1" window>
        <span v-if="diagramStore.diagram.name != '' && !autosave">
          Ausgewähltes Modell:
          <b>{{ diagramStore.diagram.name }}</b>
        </span>

        <span v-if="diagramStore.diagram.name == '' && !autosave">
          <v-icon icon="mdi-content-save-alert" class="me-1"></v-icon>
          <span> Denk immer daran dein Modell zu speichern</span>
        </span>

        <span v-if="autosave">
          <span
            >Dein Diagramm <b>{{ diagramStore.diagram.name }}</b> wurde automatisch um {{ lastAutosaveTime }} gespeichert</span
          >
        </span>

        <v-spacer></v-spacer>

        <!-- <span v-if="diagramStore.saved">
          <v-icon icon="mdi-content-save" color="success"></v-icon>
        </span>
        <span v-if="!diagramStore.saved">
          <v-icon icon="mdi-content-save-alert" color="error" class="me-1"></v-icon>
        </span> -->

        <v-spacer></v-spacer>

        <span class="ms-2">{{ currentDateTime }}</span>
      </v-system-bar>
    </div>

    <div class="left-bar">
      <FileExplorer ref="fileExplorer" class="file-explorer"></FileExplorer>
      <ToolBox class="tool-box"></ToolBox>
    </div>

    <div class="middle">
      <ModelingTool :key="modelingToolKey" :is-editable="true"></ModelingTool>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from "vue";

import ModelingTool from "@/components/ModelingTool.vue";
import FileExplorer from "@/components/FileExplorer.vue";
import ToolBox from "@/components/modelingTool/ToolBox.vue";
import DialogConfirm from "@/dialog/DialogConfirm.vue";
import { useDiagramStore } from "../stores/diagramStore";
import { storeToRefs } from "pinia";
import { useToolManagementStore } from "@/stores/toolManagementStore";
import { useRouter, onBeforeRouteLeave } from "vue-router";
import diagramService from "@/services/diagram.service";
import Diagram from "@/model/diagram/Diagram";
import { useAuthUserStore } from "../stores/authUserStore";
import evaluationService from "@/services/evaluation.service";
import taskService from "@/services/task.service";
import TaskSubmissionsResultsTabs from "@/components/TaskSubmissionsResultsTabs.vue";
import Task from "@/model/task/Task";
import FeedbackLevel from "@/enums/FeedbackLevel";
import SubmitPL from "../model/SubmitPL";
import DialogShowFeedbackVue from "@/dialog/DialogShowFeedback.vue";
import DialogAlertVue from "@/dialog/DialogAlert.vue";
import CoursePL from "@/model/course/CoursePL";
import DialogSaveToLocalStorageVue from "@/dialog/DialogSaveToLocalStorage.vue";
import DialogInactivityVue from "@/dialog/DialogInactivity.vue";
import DialogSaveDiagramVue from "@/dialog/DialogSaveDiagram.vue";
import DiagramSaveType from "@/enums/DiagramSaveType";

const authUserStore = useAuthUserStore();
const userId = ref(authUserStore.auth.user?.id!);
const selectedDiagramId = ref<number>();
const selectedDiagram = ref<Diagram>();
const fileExplorer = ref<typeof FileExplorer>();

const snackbarSuccess = ref(false);
const task = ref<Task>({} as Task);
const router = useRouter();
const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const modelingToolKey = storeToRefs(diagramStore).key;
const activeCourse = ref<CoursePL | null>(toolManagementStore.activeCourse);
const activeTask = ref<Task | null>(toolManagementStore.activeTask);
const dialogConfirm = ref<typeof DialogConfirm>();
const dialogInactivity = ref<typeof DialogInactivityVue>();
const dialogSave = ref<typeof DialogSaveDiagramVue>();

const snackbarTimer = ref<number | null>(null);
const autosaveTimer = ref<number | null>(null);
const inactivityTimer = ref<number | null>(null);

const TIMEOUT = 10 * 60 * 1000; //10 minutes timeout

const snackbarSave = ref(false);
const autosave = ref(false);
const lastAutosaveTime = ref<string>("");

const dialogSaveToLocal = ref<typeof DialogSaveToLocalStorageVue>();
const getLocalDiagram = localStorage.getItem("diagram");

if (getLocalDiagram) {
  diagramStore.diagram = JSON.parse(getLocalDiagram);
}

const currentTime = ref<Date>(new Date());
const subBtnProgress = ref<boolean>(false);
const taskSubmissionsResultsTabs = ref<typeof TaskSubmissionsResultsTabs>();
const submissionCount = ref(0);

const dialogFeedback = ref<typeof DialogShowFeedbackVue>();
const isCollapsed = ref<boolean>(true);
const alertDialog = ref<typeof DialogAlertVue>();

onMounted(() => {
  if (activeTask.value) {
    taskService.getTask(activeTask?.value.id || 0).then((response) => {
      task.value = response;
    });
  }
});

watch(toolManagementStore, (toolStore) => {
  activeTask.value = toolStore.activeTask;
  activeCourse.value = toolStore.activeCourse;
});

const triggerfeedback = () => {
  if (activeTask.value) {
    const submissionsleft = (activeTask?.value.maxSubmissions || 0) - submissionCount.value;
    dialogConfirm.value?.openDialog("Abgaben übrig: " + submissionsleft, "Möchten Sie das Diagram wirklich einreichen?", "Einreichen").then((result: boolean) => {
      if (result) {
        try {
          saveDiagram(false).then((response) => {
            const val = response as Diagram;
            selectedDiagramId.value = val.id;
            selectedDiagram.value = val;

            if (activeTask.value) {
              if (submissionIsValid()) {
                const submitPL = {} as SubmitPL;
                submitPL.diagramId = selectedDiagramId.value!;
                submitPL.taskId = activeTask?.value.id || 0;
                submitPL.userId = userId.value;
                evaluationService.submitDiagram(submitPL).then((submissionId) => {
                  waitUntilSubmissionIsEvaluated(submissionId.data).then(() => {
                    loadSubmissions();
                  });
                });
              } else {
                alertDialog.value?.openDialog("Warnung", "Leider scheint es, dass du die maximale Anzahl der erlaubten Versuche erreicht hast.");
              }
            }
          });
        } catch (error) {
          console.error("Error in saveDiagram:", error);
        }
        subBtnProgress.value = false;

        subBtnProgress.value = true;
      } else {
        subBtnProgress.value = false;
      }
    });
  }
};

setInterval(() => {
  currentTime.value = new Date();
}, 1000);

onBeforeRouteLeave((to, from, next) => {
  if (diagramStore.saved) {
    next();
    diagramStore;
  } else {
    const message = "Dein Modell wurde noch nicht gespeichert. Willst du die Seite wirklich verlassen?";
    if (window.confirm(message)) {
      next();
    } else {
      next(false);
    }
  }
});

onMounted(() => {
  if (activeTask.value) {
    evaluationService.getSubmissionIdsByUserAndTask(userId.value, activeTask?.value.id || 0).then((response) => {
      submissionCount.value = response.data.length;
    });
  }
  _setSnackbarTimer();
  _setAutosaveTimer();
  _setInactivityTimer();

  if (getLocalDiagram != null) {
    dialogSaveToLocal.value?.openDialog();
  }

  window.addEventListener("click", _setInactivityTimer);
  window.addEventListener("beforeunload", handleBeforeUnload);
});

onUnmounted(() => {
  if (toolManagementStore.highlightedEntityId != null) {
    toolManagementStore.highlightedEntityId = null;
  }
});

// watches over id and entity changes
watch(
  () => [diagramStore.diagram.id, diagramStore.diagram.entities.length],
  ([id, entities]) => {
    if (id === 0 && entities === 1) {
      // start the snackbar upon adding the first entity to the diagram
      _setSnackbarTimer();
    } else if (id === 0 && entities === 0) {
      // close snackbar if diagram is emptied
      _clearTimer(snackbarTimer);
    } else {
      // close snackbar if diagram is saved
      snackbarSave.value = false;
    }
  },
  { deep: true }
);

// check if diagram is saved to activate the autosave and inactivity timer
watch(
  () => diagramStore.diagram.id,
  (id) => {
    if (id !== 0) {
      _setAutosaveTimer();
      _clearTimer(snackbarTimer);
    } else if (id === 0) {
      //clear timers when diagram is not saved
      _clearTimer(inactivityTimer);
      _clearTimer(autosaveTimer);
      autosave.value = false;
    }
  }
);

onUnmounted(() => {
  _clearTimer(snackbarTimer);
  _clearTimer(autosaveTimer);
  _clearTimer(inactivityTimer);
  window.removeEventListener("click", _setInactivityTimer);
});

const _setSnackbarTimer = () => {
  _clearTimer(snackbarTimer);
  if (diagramStore.diagram.id === 0) {
    snackbarTimer.value = setTimeout(() => {
      snackbarSave.value = true;
    }, TIMEOUT) as unknown as number;
  }
};

const _setAutosaveTimer = () => {
  autosave.value = false;
  _clearTimer(autosaveTimer);
  if (diagramStore.diagram.id !== 0) {
    autosaveTimer.value = setInterval(() => {
      diagramStore.diagram.diagramSaveType = DiagramSaveType.AUTO;
      diagramService.putDiagram(diagramStore.diagram).then(() => {
        localStorage.removeItem("diagram");

        const actualTime = new Date();
        const hours = String(actualTime.getHours()).padStart(2, "0");
        const minutes = String(actualTime.getMinutes()).padStart(2, "0");
        lastAutosaveTime.value = `${hours}:${minutes}`;
        autosave.value = true;
      });
    }, TIMEOUT) as unknown as number;
  }
};

const _setInactivityTimer = () => {
  _clearTimer(inactivityTimer);
  if (diagramStore.diagram.id !== 0) {
    inactivityTimer.value = setTimeout(() => {
      dialogInactivity.value?.openDialog();
    }, TIMEOUT) as unknown as number;
  }
};

const _clearTimer = (timerRef: { value: number | null }) => {
  if (timerRef.value) {
    clearTimeout(timerRef.value);
  }
};

const _openSaveDialog = () => {
  dialogSave.value?.openDialog(diagramStore.diagram.id).then((result: boolean) => {
    if (result) {
      fileExplorer.value?.updateFiles();
      snackbarSave.value = false;
    } else {
      snackbarSave.value = true;
    }
  });
};

const handleBeforeUnload = (event: BeforeUnloadEvent) => {
  event.preventDefault();
  event.returnValue = "";
};

// current weekday day date year time seconds in european format
const currentDateTime = computed(() => {
  const date = currentTime.value;
  const weekday = date.toLocaleDateString("de-DE", { weekday: "long" });
  const day = date.toLocaleDateString("de-DE", { day: "2-digit" });
  const month = date.toLocaleDateString("de-DE", { month: "long" });
  const year = date.toLocaleDateString("de-DE", { year: "numeric" });
  const time = date.toLocaleTimeString("de-DE", {
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });
  return `${weekday}, ${day}. ${month} ${year} ${time}`;
});

const saveDiagram = (returntosubmission: boolean) => {
  return new Promise((resolve, reject) => {
    diagramService
      .putDiagram(diagramStore.diagram)
      .then(() => {
        localStorage.removeItem("diagram");
        diagramStore.saved = true;
        if (returntosubmission && activeTask.value && activeCourse.value) {
          router.push({ name: "ViewTask", params: { courseId: activeCourse?.value.id, taskId: activeTask?.value.id } });
        }
        resolve(diagramStore.diagram); // Resolve the promise when the diagram is successfully put
      })
      .catch((error) => {
        alert("Diagramm konnte nicht gespeichert werden");
        reject(error); // Reject the promise if there's an error
      });
  });
};

// Function to convert Date string into a Date object
const convertDate = (dueDateStr: any) => {
  const [day, month, year, time] = dueDateStr.split(/\.|\s/);
  return new Date(`${year}-${month}-${day} ${time}`);
};

const loadSubmissions = () => {
  if (activeTask.value) {
    evaluationService.getSubmissionIdsByUserAndTask(userId.value, activeTask?.value.id || 0).then((response) => {
      const submissionIds = response.data;
      submissionCount.value = submissionIds.length;
      taskSubmissionsResultsTabs.value?.load(task.value);
    });
  }
};

const waitUntilSubmissionIsEvaluated = (submissionId: number) => {
  return new Promise((resolve) => {
    const interval = setInterval(() => {
      evaluationService.getSubmissionById(submissionId, FeedbackLevel.NOTHING).then((response) => {
        if (response.status == 200) {
          clearInterval(interval);
          resolve(response.data);
        }
      });
    }, 1000);
  });
};

const submissionIsValid = () => {
  if (activeTask.value) {
    const date = convertDate(activeTask.value.dueDate);
    const actual_date = new Date();
    return selectedDiagramId.value && (activeTask?.value.maxSubmissions as number) > submissionCount.value && actual_date < date;
  } else {
    return false;
  }
};

const collapseFeedback = () => {
  if (isCollapsed.value) {
    isCollapsed.value = false;
    loadSubmissions();
  } else {
    isCollapsed.value = true;
  }
};

const openFeedback = () => {
  dialogFeedback.value?.openDialog();
};
</script>

<style scoped lang="scss">
.container {
  position: relative;
  width: 100%;
  height: 120vh;
}

.container {
  display: grid;
  grid-template-columns: 200px 1fr;
  grid-template-rows: 0px 1fr;
  grid-template-areas:
    "header header header"
    "left middle right";
}

.navigation {
  grid-area: header;
  // background-color: #c9c6c6;
}

.left-bar {
  grid-area: left;
  // background-color: #b19e9e;
}

.middle {
  grid-area: middle;
  background-color: #564848;
  position: relative;
  width: 100%;
  height: 100%;
}

.right-bar {
  grid-area: right;
  max-width: 500px;
  // background-color: #d1aaaa;
}

.task-floater {
  position: absolute;
  top: 10px;
  right: 25px;
  z-index: 10;
  max-height: calc(100vh - 20px);
  max-width: 70vh;
  overflow-y: auto;
}

.submissions-results {
  margin-top: -10px;
}

.widget-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.widget {
  width: 30px;
  height: 22px;
  padding: 2px;
  margin: 2px;
  cursor: pointer;
}

// .file-explorer {
//   position: absolute;
//   top: 80px;
//   left: 10px;
// }

// .tool-box {
//   position: absolute;
//   top: 260px;
//   left: 10px;
// }
</style>
