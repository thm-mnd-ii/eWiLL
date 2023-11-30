<template>
  <v-dialog v-model="InactivityDialog" persistent width="680px">
    <v-card class="dialog-card">
      <v-card-title> Automatische Speicherung </v-card-title>
      <v-card-text class="prompt-text"> Dein Modell wurde automatisch gespeichert. Bitte teile uns mit, was du gerade machst, damit wir deinen Lernweg besser verstehen und dich unterstützen können. </v-card-text>
      <v-card-actions>
        <v-btn variant="text" @click="saveDiagram(DiagramSaveType.THINKING)"> Ich überlege gerade </v-btn>
        <v-btn variant="text" @click="saveDiagram(DiagramSaveType.BREAK)"> Ich mache einer Lernpause </v-btn>
        <v-btn variant="text" @click="saveDiagram(DiagramSaveType.DISTRACTED)"> Ich war abgelenkt </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useDiagramStore } from "../stores/diagramStore";
import { useAuthUserStore } from "../stores/authUserStore";
import DiagramSaveType from "@/enums/DiagramSaveType";
import diagramService from "../services/diagram.service";

const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();

const InactivityDialog = ref<boolean>(false);

const saveDiagram = (saveType: DiagramSaveType) => {
  // set user id of diagram owner
  if (authUserStore.auth.user != null) {
    diagramStore.diagram.ownerId = authUserStore.auth.user?.id; //Add to auto save?
    diagramStore.diagram.diagramSaveType = saveType;

    diagramService
      .putDiagram(diagramStore.diagram)
      .then(() => {
        _close();
      })
      .catch((error) => {
        console.log(error);
        alert("Diagramm konnte nicht gespeichert werden");
      });
  } else {
    throw new Error("User is not logged in");
  }
};

// Promis
const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = () => {
  InactivityDialog.value = true;
  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _close = () => {
  InactivityDialog.value = false;
  resolvePromise.value(false);
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped lang="scss"></style>
