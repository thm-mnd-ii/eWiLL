<template>
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Diagramm erfolgreich eingereicht </v-snackbar>
  <DialogConfirm ref="dialogConfirm" />
  <div v-if="activeTask != undefined" class="task-floater">
    <span>Aktuelle Aufgabe: {{ activeTask?.name }}</span
    ><br />
    <span>Aus Kurs: {{ activeCourse?.name }}</span
    ><br />
    <v-btn @click="submitDiagram">Einreichen</v-btn>
  </div>
  <div class="container">
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

        <!-- <v-icon icon="mdi-wifi-strength-4"></v-icon>
        <v-icon icon="mdi-signal" class="ms-2"></v-icon>
        <v-icon icon="mdi-battery" class="ms-2"></v-icon> -->

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

import { useDiagramStore } from "../stores/diagramStore";
import { storeToRefs } from "pinia";
import { useToolManagementStore } from "@/stores/toolManagementStore";
import { useAuthUserStore } from "../stores/authUserStore";

import evaluationService from "@/services/evaluation.service";

import DialogConfirm from "@/dialog/DialogConfirm.vue";
import SubmitPL from "@/model/SubmitPL";

const dialogConfirm = ref<typeof DialogConfirm>();
const snackbarSuccess = ref(false);

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();
const authUserStore = useAuthUserStore();

const modelingToolKey = storeToRefs(diagramStore).key;
const activeCourse = toolManagementStore.activeCourse;
const activeTask = toolManagementStore.activeTask;

const currentTime = ref<Date>(new Date());
setInterval(() => {
  currentTime.value = new Date();
}, 1000);

const warnBeforeUnload = () => {
  window.addEventListener("beforeunload", (event) => {
    event.preventDefault();
  });
};

onMounted(() => {
  warnBeforeUnload();
});

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
  dialogConfirm.value?.openDialog("Abgabe: " + diagramStore.diagram.name, "Möchten Sie das Diagram wirklich für die Aufgabe " + activeTask?.name + "des Kurses " + activeCourse?.name + " einreichen?", "Einreichen").then((result: boolean) => {
    if (result) {
      const submitPL = {} as SubmitPL;
      submitPL.diagramId = diagramStore.diagram.id;
      submitPL.taskId = activeTask!.id;
      submitPL.userId = authUserStore.auth.user?.id!;
      evaluationService.submitDiagram(submitPL).then(() => {
        snackbarSuccess.value = true;
      });
    }
  });
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
  bottom: 300px;
  z-index: 99;
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
