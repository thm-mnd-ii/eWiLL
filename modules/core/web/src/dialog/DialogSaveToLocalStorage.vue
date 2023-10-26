<template>
  <DialogShowFullDiagramVue ref="dialogShowFull"></DialogShowFullDiagramVue>
  <v-dialog v-model="saveDialog" width="75%">
    <v-card>
      <v-card-title>
        <span class="text-h5">Möchtest du das Diagramm lokal speichern?</span>
      </v-card-title>

      <v-card class="preview-container">
        <h5 class="warning">Bitte beachte, dass das Diagramm dann nur auf deinem Gerät und nicht auf dem Server gespeichert wird.</h5>
          <v-card-title class="task-header-title">
            <h6 class="headline mb-0">Vorschau:</h6>
            <v-spacer></v-spacer>
            <v-btn icon variant="text" color="dark-gray" @click="openFullDiagram">
              <v-icon icon="mdi-fullscreen" size="x-large"></v-icon>
            </v-btn>
          </v-card-title>
          <v-card-text class="modeling-container">
            <ModelingTool :key="modelingToolKey" class="modelPreview" :is-editable="false"></ModelingTool>
          </v-card-text>
        </v-card>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="_close"> Schließen </v-btn>
        <v-btn variant="text" @click="saveDiagram"> Speichern </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>


<script setup lang="ts">
import { ref } from 'vue';
import { useDiagramStore } from '@/stores/diagramStore';
import { useAuthUserStore } from "../stores/authUserStore";
import DialogShowFullDiagramVue from './DialogShowFullDiagram.vue';
import ModelingTool from "@/components/ModelingTool.vue";
import { storeToRefs } from "pinia";

const saveDialog = ref<boolean>(false);
const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();

const modelingToolKey = storeToRefs(diagramStore).key;

const dialogShowFull = ref<typeof DialogShowFullDiagramVue>();

const saveDiagram = () => {  
  if (authUserStore.auth.user != null) {
    diagramStore.diagram.ownerId = authUserStore.auth.user?.id;
  } else {
    throw new Error("User is not logged in");
  }

  localStorage.setItem('diag', JSON.stringify(diagramStore.diagram));
  _close();
};

const openFullDiagram = () => {
  dialogShowFull.value?.openDialog("");
};

const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = () => {
  saveDialog.value = true;

  return new Promise<boolean>((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _close = () => {
  saveDialog.value = false;
  resolvePromise.value(false);
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped lang="scss">

.warning {
  padding: 15px 0 0 15px;
}
.task-header-title {
  display: flex;
  align-items: center;
}

.modelPreview {
  position: relative;
  width: 100%;
  height: 100%;
}

.modeling-container {
  width: 100%;
  height: 350px;
}
</style>