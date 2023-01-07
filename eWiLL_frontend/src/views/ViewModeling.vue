<template>
  <div class="container">
    <v-btn @click="toggleMode">Toggle Mode</v-btn>

    <FileExplorer class="file-explorer"></FileExplorer>

    <ModelingTool :key="modelingToolKey"></ModelingTool>
  </div>
</template>

<script setup lang="ts">
import ModelingTool from "../components/ModelingTool.vue";
import FileExplorer from "../components/FileExplorer.vue";
import Mode from "../enums/Mode";

import { useToolManagementStore } from "../stores/toolManagementStore";
import { useDiagramStore } from "../stores/diagramStore";
import { storeToRefs } from "pinia";

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const modelingToolKey = storeToRefs(diagramStore).key;

const toggleMode = () => {
  if (toolManagementStore.mode == Mode.EDIT) {
    toolManagementStore.mode = Mode.VIEW;
  } else {
    toolManagementStore.mode = Mode.EDIT;
  }
};
</script>

<style scoped lang="scss">
.container {
  position: relative;
  width: 100%;
  height: 100vh;
  margin-bottom: 5%;
}

.file-explorer {
  position: absolute;
  top: 80px;
  left: 10px;
}
</style>
