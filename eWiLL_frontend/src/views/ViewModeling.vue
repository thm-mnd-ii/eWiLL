<template>
  <v-btn @click="toggleMode">Toggle Mode</v-btn>
  <ModelingTool></ModelingTool>
</template>

<script setup lang="ts">
import ModelingTool from "../components/ModelingTool.vue";
import { useToolManagementStore } from "../stores/toolManagementStore";
import Mode from "../enums/Mode";
const toolManagementStore = useToolManagementStore();
const toggleMode = () => {
  if (toolManagementStore.mode == Mode.EDIT) {
    toolManagementStore.mode = Mode.VIEW;
  } else {
    toolManagementStore.mode = Mode.EDIT;
  }
};
</script>

<style scoped lang="scss"></style>
