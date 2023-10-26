<template>
  <v-dialog v-model="saveDialog" width="75%">
    <v-card>
      <v-card-title>
        <span class="text-h5">Save Diagram</span>
      </v-card-title>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="_close"> Close </v-btn>
        <v-btn variant="text" @click="saveDiagram"> Save </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>


<script setup lang="ts">
import { ref } from 'vue';
import { useDiagramStore } from '@/stores/diagramStore';
import { useAuthUserStore } from "../stores/authUserStore";

const saveDialog = ref<boolean>(false);
const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();

const saveDiagram = () => {  
  if (authUserStore.auth.user != null) {
    diagramStore.diagram.ownerId = authUserStore.auth.user?.id;
  } else {
    throw new Error("User is not logged in");
  }

  localStorage.setItem('diag', JSON.stringify(diagramStore.diagram));
  _close();
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
.save-diagram-form {
  // grid in one row
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr;
  grid-template-areas: "name category category";
  grid-column-gap: 10px;
  grid-row-gap: 10px;
  // align items
  align-items: center;
  // set width
  width: 100%;
}
</style>