<template>
  <v-dialog v-model="AutoSaveDialog" persistent width="42%">
    <v-card class="dialog-card">
      <v-card-title>
        <span class="text-h5">Automatische Speicherung</span>
      </v-card-title>
      <v-card-text class="prompt-text">
        Dein Modell wurde automatisch gespeichert. 
        Bitte teile uns mit, was du gerade machst, damit wir deinen Lernweg besser verstehen und unterstützen können.
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
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

const AutoSaveDialog = ref<boolean>(false);

const saveDiagram = (saveType: DiagramSaveType) => {
  // set user id of diagram owner
  if (authUserStore.auth.user != null) {
    diagramStore.diagram.ownerId = authUserStore.auth.user?.id;
    console.log("BEFORE ", diagramStore.diagram.diagramSaveType, diagramStore.diagram.id);
    diagramStore.diagram.diagramSaveType = saveType;
    console.log("AFTER ", diagramStore.diagram.diagramSaveType);

    diagramService.postDiagram(diagramStore.diagram).then((result) => {
          diagramStore.diagram.id = result.data;
          console.log(result.config.data, diagramStore.diagram.id);
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
  AutoSaveDialog.value = true;
  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _close = () => {
  AutoSaveDialog.value = false;
  resolvePromise.value(false);
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped lang="scss">
.dialog-card{
    height: 250px;
    .prompt-text {
        margin-top: 35px;
        line-height: 1.6; 
        white-space: normal;
    }
}
</style>
  