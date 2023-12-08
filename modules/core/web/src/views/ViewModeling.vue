<template>
  <DialogConfirm ref="dialogConfirm" />
  <DialogShowFeedbackVue ref="dialogFeedback"></DialogShowFeedbackVue>
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Diagramm erfolgreich eingereicht </v-snackbar>
  
  <div class="container" >
    <v-card v-if="activeTask != undefined" class="task-floater" elevation="3">
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
            <v-btn @click="submitDiagram(true)">Zurück zur Abgabe</v-btn>
            <v-btn v-if="isStudent && !subBtnProgress" @click="triggerfeedback()">Prüfen</v-btn>
            <v-progress-circular v-if="subBtnProgress" indeterminate></v-progress-circular>
          </v-col>
        </v-row>
      </v-card-actions>
      <div class="submissions-results"> 
        <div class="widget-bar">      
        <v-card-title v-if="showFeedback">Feedback</v-card-title>
        <div>
          <v-icon v-if="showFeedback" class="widget" icon="mdi-arrow-expand" size="x-small" @click="openFeedback()"></v-icon>
          <v-icon v-if="showFeedback && isCollapsed" class="widget" icon="mdi-arrow-collapse-down" size="x-small" @click="collapseFeedback()"></v-icon>
          <v-icon v-if="showFeedback && !isCollapsed" class="widget" icon="mdi-arrow-expand-up" size="x-small" @click="collapseFeedback()"></v-icon>
        </div>
      </div>

        <TaskSubmissionsResultsTabs v-if="showFeedback && !isCollapsed" ref="taskSubmissionsResultsTabs"></TaskSubmissionsResultsTabs>

    </div>
    </v-card>

    <div class="navigation">
      <v-system-bar color="white" elevation="1" window>
        <span v-if="diagramStore.diagram.name != ''">
          Ausgewähltes Modell:
          <b>{{ diagramStore.diagram.name }}</b>
        </span>

        <span v-if="diagramStore.diagram.name == ''">
          <v-icon icon="mdi-content-save-alert" class="me-1"></v-icon>
          <span> Denk immer daran dein Modell zu speichern</span>
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
      <FileExplorer class="file-explorer"></FileExplorer>
      <ToolBox class="tool-box"></ToolBox>
    </div>

    <div class="middle">
      <ModelingTool :key="modelingToolKey" :is-editable="true"></ModelingTool>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
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
import { onUnmounted } from "vue";

const authUserStore = useAuthUserStore();
const userId = ref(authUserStore.auth.user?.id!);
const selectedDiagramId = ref<number>();
const selectedDiagram = ref<Diagram>();
const snackbarSuccess = ref(false);
const task = ref<Task>({} as Task);
const router = useRouter();
const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const modelingToolKey = storeToRefs(diagramStore).key;
const activeCourse = toolManagementStore.activeCourse;
const activeTask = toolManagementStore.activeTask
const dialogConfirm = ref<typeof DialogConfirm>();
const currentTime = ref<Date>(new Date());
const subBtnProgress = ref<boolean>(false);
const taskSubmissionsResultsTabs = ref<typeof TaskSubmissionsResultsTabs>();
const submissionCount = ref(0);
      
const showFeedback = ref(false);
const dialogFeedback = ref<typeof DialogShowFeedbackVue>();
const isCollapsed = ref<boolean>(false);

const isStudent = computed(() => {
  return localStorage.getItem("role") === "ROLE_ADMIN";
});

const triggerfeedback = () => {
  const submissionsleft = (activeTask?.maxSubmissions || 0) - submissionCount.value;
  dialogConfirm.value?.openDialog("Abgaben übrig: "+submissionsleft, 'Möchten Sie das Diagram wirklich einreichen? :', "Einreichen").then((result: boolean) => {
  if (result) {
    saveandcheckdiag();
    showFeedback.value = true;
    subBtnProgress.value = true;
  }
  else{
    showFeedback.value = false;
    subBtnProgress.value = false;
  }
  });
}

setInterval(() => {
  currentTime.value = new Date();
}, 1000);

onBeforeRouteLeave((to, from, next) => {
  if (diagramStore.saved) {
    next();diagramStore
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
  evaluationService.getSubmissionIdsByUserAndTask(userId.value, activeTask?.id || 0).then((response) => {
    submissionCount.value = response.data.length;
  });
  window.addEventListener("beforeunload", handleBeforeUnload);
});

onUnmounted(() => {
  if (toolManagementStore.highlightedEntityId != null) {
      toolManagementStore.highlightedEntityId = null;
    }
});


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



const submitDiagram = (returntosubmission:boolean) => {
  return new Promise((resolve, reject) => {
    diagramService.putDiagram(diagramStore.diagram)
      .then(() => {
        diagramStore.saved = true;
        if (returntosubmission) {
          router.push({ name: "ViewTask", params: { courseId: activeCourse?.id, taskId: activeTask?.id } });
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
function convertDate(dueDateStr:any) {
  const [day, month, year, time] = dueDateStr.split(/\.|\s/);
  return new Date(`${year}-${month}-${day} ${time}`);
}


const saveandcheckdiag = async () => {
  try {
    await submitDiagram(false).then((response) => {
      const val= response as Diagram;
      selectedDiagramId.value = val.id;
      selectedDiagram.value = val;
    })
 await  checkdiagramm();
  } catch (error) {
    console.error("Error in submitDiagram:", error);
  }
subBtnProgress.value = false;
};


const loadTask =  () => {
  taskService.getTask(activeTask?.id || 0).then((response) => {
    task.value = response;
    
  });
};
    
const loadSubmissions =   (selectedTaskIndex?: Number) => { 
    
    evaluationService.getSubmissionIdsByUserAndTask(userId.value, activeTask?.id || 0 ).then((response) => {
    const submissionIds = response.data;
    submissionCount.value = submissionIds.length;
    taskSubmissionsResultsTabs.value?.load(task.value, selectedTaskIndex);
});
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

const checkdiagramm = () => {
  loadTask();
  const date = convertDate(activeTask?.dueDate);
  const actual_time = convertDate(new Date().toString());

  if (selectedDiagramId.value && (activeTask?.maxSubmissions as number > submissionCount.value) && ( actual_time < date ) ) {
    const submitPL = {} as SubmitPL;
    submitPL.diagramId = selectedDiagramId.value!;
    submitPL.taskId = activeTask?.id || 0;
    submitPL.userId = userId.value;
    evaluationService.submitDiagram(submitPL).then((submissionId) => {
      waitUntilSubmissionIsEvaluated(submissionId.data).then(() => {
        loadSubmissions();
      });
    });
  } else {
    alert("Maximale Anzahl an Abgaben erreicht");
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
  height: 100vh;
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
