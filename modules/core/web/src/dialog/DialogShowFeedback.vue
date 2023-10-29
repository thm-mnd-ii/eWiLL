<template>
    <v-dialog v-model="deleteDialog" width="100vw" height="100vh">
      <v-card :title="Title" class="modelPreview" variant="outlined" width="100%" height="100%">
        <div class="message-container">
          <div v-html="Message"></div>
        </div>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="_cancel">Schließen</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </template>
  
  
  
  <script setup lang="ts">
  import { ref } from "vue";
  
  

  
  const deleteDialog = ref<boolean>(false);
  const Title = ref<string>("");
  const Message = ref<string | undefined>(undefined);
  
  // Promises
  const resolvePromise: any = ref(undefined);
  const rejectPromise: any = ref(undefined);
  
  const openDialog = (title: string, message: string) => {
    Title.value = title;
    Message.value = message; 
    deleteDialog.value = true;
  
    return new Promise((resolve, reject) => {
      resolvePromise.value = resolve;
      rejectPromise.value = reject;
    });
  };
  

  
  const _cancel = () => {
    deleteDialog.value = false;
    resolvePromise.value(false);
  };
  
  defineExpose({
    openDialog,
  });
  </script>
  
  <style scoped>
  .modelPreview {
    position: relative;
    width: 100%;
    height: 100%;
    background-color: lavender;
    z-index: 0;
    overflow: auto;
    display: flex;
    flex-direction: column;
  }
  .message-container {
    flex-grow: 1;
  }
  </style>
  