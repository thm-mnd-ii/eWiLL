<template>
  <v-dialog v-model="deleteDialog" width="100vw" height="100vh">
    <v-card :title="deleteTitle" :text="deleteMessage" width="100%" height="100%">
      <v-card-text>
        <ModelingTool class="modelPreview" :is-editable="false"></ModelingTool>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="_cancel"> Schließen </v-btn>
        <!-- <v-btn variant="text" @click="_confirm"> {{ deleteConfirmBtnText }} </v-btn> -->
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import ModelingTool from "@/components/ModelingTool.vue";

const deleteDialog = ref<boolean>(false);
const deleteTitle = ref<string>("");
const deleteMessage = ref<string | undefined>(undefined);
// const deleteConfirmBtnText = ref<string | undefined>("Löschen");

// Promis
const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = (title: string) => {
  deleteTitle.value = title;
  deleteDialog.value = true;

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

// const _confirm = () => {
//   deleteDialog.value = false;
//   resolvePromise.value(true);
// };

const _cancel = () => {
  deleteDialog.value = false;
  resolvePromise.value(false);
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped>
.modelPreview {
  position: relative;
  width: 100%;
  height: 100%;
}
</style>
