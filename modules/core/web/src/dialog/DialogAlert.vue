<template>
    <v-dialog v-model="alertDialog" width="500px">
      <v-card :title="alertTitle" :text="alertMessage">
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="_cancel"> OK </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </template>
  
  <script setup lang="ts">
  import { ref } from "vue";
  
  const alertDialog = ref<boolean>(false);
  const alertTitle = ref<string>("");
  const alertMessage = ref<string | undefined>(undefined);
  
  // Promis
  const resolvePromise: any = ref(undefined);
  const rejectPromise: any = ref(undefined);
  
  const openDialog = (title: string, message: string | undefined) => {
    alertTitle.value = title;
    alertMessage.value = message;
    alertDialog.value = true;
  
    return new Promise((resolve, reject) => {
      resolvePromise.value = resolve;
      rejectPromise.value = reject;
    });
  };
  
  const _cancel = () => {
    alertDialog.value = false;
    resolvePromise.value(false);
  };
  
  // define expose
  defineExpose({
    openDialog,
  });
  </script>
  
  <style scoped></style>
  