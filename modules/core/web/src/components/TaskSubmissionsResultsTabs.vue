<template>
  <v-card class="task-trials-tabs">
    <v-tabs v-model="selectedResultTab" bg-color="teal-darken-3" slider-color="teal-lighten-3">
      <v-tab v-for="tab in submissions" :key="tab.attempt" :value="tab.attempt">
        {{ "Ergebnis " + tab.attempt }}
      </v-tab>
    </v-tabs>
    <v-window v-model="selectedResultTab">
      <v-window-item v-for="tab in submissions" :key="tab.attempt" :value="tab.attempt">
        <v-card flat>
          <v-card-text class="task-trials-text">
            <v-chip v-if="selectedResult.correct" color="green" append-icon="mdi-check-circle" class="margin-right-5px">Korrekt: </v-chip>
            <v-chip v-if="!selectedResult.correct" color="red" append-icon="mdi-close-circle" class="margin-right-5px">Fehlerhaft: </v-chip>
            <v-chip v-if="selectedResult.correct" color="green">Score: {{ selectedResult.score }}</v-chip>
            <v-chip v-if="!selectedResult.correct" color="red">Score: {{ selectedResult.score }}</v-chip>
            <br />
            <br />
            <p v-for="comment in selectedResult.comments" :key="comment.message">{{ comment.message }}</p>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn class="" append-icon="mdi-open-in-new" color="dark-gray" variant="text" @click="showDiagramWithMistakes"> Zeige Fehler im Diagram </v-btn>
          </v-card-actions>
        </v-card>
      </v-window-item>
    </v-window>
  </v-card>
  <DialogShowFullDiagram ref="dialogShowFullDiagram" />
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { useAuthUserStore } from "../stores/authUserStore";
import { useDiagramStore } from "@/stores/diagramStore";
// Models
import Submission from "../model/submission/Submission";
import Diagram from "../model/diagram/Diagram";
import Task from "../model/task/Task";
// Services
import submissionService from "@/services/submission.service";
//Dialogs
import DialogShowFullDiagram from "@/dialog/DialogShowFullDiagram.vue";
import Result from "@/model/submission/Result";

const authUserStore = useAuthUserStore();
const diagramStore = useDiagramStore();
const userId = ref(authUserStore.auth.user?.id!);

const currentTask = ref<Task>({} as Task);

const selectedResultTab = ref<number>(1);
const dialogShowFullDiagram = ref<typeof DialogShowFullDiagram>();

const submissions = ref<Submission[]>([] as Submission[]);
const selectedResult = ref<Result>({} as Result);

watch(
  () => selectedResultTab.value,
  (newValue) => {
    console.log(newValue);
    submissionService.getResultBySubmissionIdAndLevel(submissions.value[newValue - 1].id, currentTask.value.showLevel).then((response) => {
      selectedResult.value = response.data;
    });
  }
);

const load = (task: Task) => {
  currentTask.value = task;

  submissionService.getSubmissionIdsByUserAndTask(userId.value, task.id).then((response) => {
    submissions.value = response.data;
    console.log(submissions.value);

    //select last tab
    selectedResultTab.value = submissions.value.length;
  });
};

const showDiagramWithMistakes = () => {
  diagramStore.loadDiagram(submissions.value[selectedResultTab.value - 1].diagram as Diagram);
  dialogShowFullDiagram.value?.openDialog("");
};

defineExpose({
  load,
});
</script>

<style scoped>
.task-trials-tabs {
  margin: 20px 0;
  width: 100%;
}

.task-trials-text {
  min-height: 100px;
}

.margin-right-5px {
  margin-right: 5px;
}
</style>
