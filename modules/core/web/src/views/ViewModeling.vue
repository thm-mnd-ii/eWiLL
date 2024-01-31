<template>
  <DialogInactivityVue ref="dialogInactivity"></DialogInactivityVue>
  <DialogSaveDiagramVue ref="dialogSave"></DialogSaveDiagramVue>
  <DialogSaveToLocalStorageVue ref="dialogSaveToLocal"></DialogSaveToLocalStorageVue>
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Diagramm erfolgreich eingereicht </v-snackbar>
  <v-snackbar v-model="snackbarSave" vertical location="right bottom" color="#b03333" :timeout="-1">
    <div class="text-subtitle-1 pb-2">Achtung.. Dein Modell ist noch nicht gespeichert!</div>
    <p>Bitte speichere deine Arbeit, damit keine Änderungen verloren gehen.</p>

    <template #actions>
      <v-btn color="white" variant="text" @click="_openSaveDialog"> Zum Speichern </v-btn>
    </template>
  </v-snackbar>

  <div class="container">
    <v-card v-if="activeTask != undefined" class="task-floater" elevation="3">
      <v-card-title>Abgabe</v-card-title>
      <v-card-subtitle>
        <span>Aktuelle Aufgabe: {{ activeTask?.name }}</span
        ><br />
        <span>Aus Kurs: {{ activeCourse?.name }}</span
        ><br />
      </v-card-subtitle>
      <v-card-actions>
        <v-btn @click="submitDiagram">Zurück zur Abgabe</v-btn>
      </v-card-actions>
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

import { useDiagramStore } from "../stores/diagramStore";
import { storeToRefs } from "pinia";
import { useToolManagementStore } from "@/stores/toolManagementStore";
import { useRouter, onBeforeRouteLeave } from "vue-router";

import diagramService from "@/services/diagram.service";

import DialogSaveToLocalStorageVue from "@/dialog/DialogSaveToLocalStorage.vue";
import DialogInactivityVue from "@/dialog/DialogInactivity.vue";
import DialogSaveDiagramVue from "@/dialog/DialogSaveDiagram.vue";
import DiagramSaveType from "@/enums/DiagramSaveType";

const fileExplorer = ref<typeof FileExplorer>();

const snackbarSuccess = ref(false);

const router = useRouter();
const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const modelingToolKey = storeToRefs(diagramStore).key;
const activeCourse = toolManagementStore.activeCourse;
const activeTask = toolManagementStore.activeTask;
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
setInterval(() => {
  currentTime.value = new Date();
}, 1000);

onBeforeRouteLeave((to, from, next) => {
  if (diagramStore.saved) {
    next();
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
  _setSnackbarTimer();
  _setAutosaveTimer();
  _setInactivityTimer();

  if (getLocalDiagram != null) {
    dialogSaveToLocal.value?.openDialog();
  }

  window.addEventListener("click", _setInactivityTimer);
  window.addEventListener("beforeunload", handleBeforeUnload);
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

const submitDiagram = () => {
  //save diagram
  diagramService
    .putDiagram(diagramStore.diagram)
    .then(() => {
      toolManagementStore.activeCourse = null;
      toolManagementStore.activeTask = null;

      diagramStore.saved = true;
      router.push({ name: "ViewTask", params: { courseId: activeCourse?.id, taskId: activeTask?.id } });
    })
    .catch(() => {
      alert("Diagramm konnte nicht gespeichert werden");
    });

  // //navigate to ViewTask
  // router.push({ name: "ViewTask", params: { courseId: activeCourse?.id, taskId: activeTask?.id } });
};
</script>

<style scoped lang="scss">
.container {
  position: relative;
  width: 100%;
  height: 100%;
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
  // background-color: #d1aaaa;
}

.task-floater {
  position: absolute;
  top: 20px;
  right: 25px;
  z-index: 10;
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
