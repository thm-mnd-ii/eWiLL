<template>
  <v-card class="task-trials-tabs">
    <v-tabs v-model="selectedResultTab" bg-color="teal-darken-3" slider-color="teal-lighten-3">
      <v-tab v-for="tab in submissionsWithResults" :key="tab.submission.id" :value="tab.submission.id">
        {{ "Ergebnis " + tab.submission.attempt }}
      </v-tab>
    </v-tabs>
    <v-window v-model="selectedResultTab">
      <v-window-item v-for="tab in submissionsWithResults" :key="tab.submission.id" :value="tab.submission.id">
        <v-card flat>
          <v-card-text class="task-trials-text">
            <v-chip v-if="tab.result.correct" color="green" append-icon="mdi-check-circle" class="margin-right-5px">Korrekt: </v-chip>
            <v-chip v-if="!tab.result.correct" color="red" append-icon="mdi-close-circle" class="margin-right-5px">Korrekt: </v-chip>
            <v-chip v-if="tab.result.correct" color="green">Score: {{ tab.result.score }}</v-chip>
            <v-chip v-if="!tab.result.correct" color="red">Score: {{ tab.result.score }}</v-chip>
            <br />
            <br />
            <p>Tab {{ tab.result.comments[0] }}</p>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn class="" append-icon="mdi-open-in-new" color="dark-gray" variant="text"> Zeige Fehler im Diagram </v-btn>
          </v-card-actions>
        </v-card>
      </v-window-item>
    </v-window>
  </v-card>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useAuthUserStore } from "../stores/authUserStore";
// Models
import Submission from "../model/submission/Submission";
import SubmissionAndResult from "../model/submission/SubmissionAndResult";
// Services
import submissionService from "@/services/submission.service";

const authUserStore = useAuthUserStore();
const userId = ref(authUserStore.auth.user?.id!);

const selectedResultTab = ref();

const submissions = ref<Submission[]>();
const submissionsWithResults = ref<SubmissionAndResult[]>();

const load = (taskId: number) => {
  submissionsWithResults.value = [];
  submissionService.getSubmissionIdsByUserAndTask(userId.value, taskId).then((response) => {
    submissions.value = response.data;
    submissions.value?.forEach((submission) => {
      submissionService.getResultBySubmissionId(submission.id).then((response) => {
        let tmp = {} as SubmissionAndResult;
        tmp.submission = submission;
        tmp.result = response.data;
        // This ensures the items are sorted by the attempt
        submissionsWithResults.value?.splice(tmp.submission.attempt - 1, 0, tmp);
      });
    });
    console.log(submissionsWithResults.value);
  });
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
