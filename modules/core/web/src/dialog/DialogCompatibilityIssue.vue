<template>  
  <v-dialog v-model="compDialog" width="50%">
    <v-card>
      <v-card-title>
        <span class="text-h5">Kompatibilitätsproblem</span>
        <v-card-text class="comp-text">
          <strong>Leider haben wir festgestellt, dass dein aktuelles Browser- oder Betriebssystem nicht mit unserem Modellierungstool kompatibel ist. 
            Zusätzlich erfordert unser Tool eine Mindestbildschirmgröße!</strong>
          <br />
          <br />
          Für die beste Benutzererfahrung und volle Funktionalität, stelle bitte sicher, dass du ein unterstütztes Desktop-Betriebssystem und einen
           kompatiblen Browser benutzt und dass dein Bildschirm die erforderliche Mindestgröße aufweist:
          <br />
        <ul>
            <li><strong>Betriebssysteme:</strong> <i>Windows, macOS, oder Linux</i></li>
            <li><strong>Browser:</strong> <i>Chromium basiert oder Mozilla Firefox</i></li>
            <li><strong>Mindestbildschirmgröße:</strong> <i>800 x 400</i></li>
        </ul>
          Bitte beachte, dass unser Tool <strong>nicht</strong> für die Nutzung auf <strong>mobilen Geräten oder Tablets</strong> optimiert ist. 
        </v-card-text>
      </v-card-title>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="_close"> Schließen </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";

const compDialog = ref<boolean>(false);

const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = () => {
  compDialog.value = true;

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _close = () => {
  compDialog.value = false;
  resolvePromise.value(false);
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped lang="scss">
.comp-text {
    margin-top: 20px;
    padding: 20px 24px;
    font-size: 16px;
    line-height: 1.6; 
    white-space: normal;
    
    ul {
      padding-left: 30px;
      margin-top: 20px;
    }

    li {
      margin-bottom: 20px;
    }
}
</style>
  