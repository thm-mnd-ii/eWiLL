<template>
  <DialogShowFullDiagramVue ref="dialogShowFull"></DialogShowFullDiagramVue>
  <v-dialog v-model="saveDialog" width="75%">
    <v-card>
      <v-card-title>
        <span class="text-h5">Möchtest du dieses Diagramm übernehmen?</span>
      </v-card-title>

      <v-card class="preview-container">
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
        <v-btn variant="text" @click="_newDiagram"> Modell verwerfen </v-btn>
        <v-btn variant="text" @click="_loadDiagram"> Übernehmen </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>


<script setup lang="ts">
import { ref } from 'vue';
import { useDiagramStore } from '@/stores/diagramStore';
import DialogShowFullDiagramVue from './DialogShowFullDiagram.vue';
import ModelingTool from "@/components/ModelingTool.vue";
import { storeToRefs } from "pinia";
import { useAuthUserStore } from '@/stores/authUserStore';

const saveDialog = ref<boolean>(false);
const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();

const modelingToolKey = storeToRefs(diagramStore).key;

const dialogShowFull = ref<typeof DialogShowFullDiagramVue>();

const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const _loadDiagram = () => {  
  if (authUserStore.auth.user != null) {
    diagramStore.diagram.ownerId = authUserStore.auth.user?.id;
  } else {
    throw new Error("User is not logged in");
  }
  _close();
};

const _newDiagram = () => {
  localStorage.removeItem("diagram");
  diagramStore.createNewDiagram();
  _close();
};

const openFullDiagram = () => {
  dialogShowFull.value?.openDialog();
};

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