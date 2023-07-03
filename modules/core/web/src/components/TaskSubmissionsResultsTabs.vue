<!-- eslint-disable vue/valid-v-slot -->
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
            <!-- <p v-for="comment in selectedResult.comments" :key="comment.message">{{ comment.message }}</p> -->
            <v-data-table :group-by="groupBy" :headers="headers" :items="selectedResult.comments" :sort-by="sortBy" class="elevation-1" item-value="name">
              <template #item.highlightLevel="{ item }">
                <v-chip label :color="getHighlightLevelColor(item.value.highlightLevel)">
                  {{ item.value.highlightLevel }}
                </v-chip>
              </template>
            </v-data-table>
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

const sortBy = ref<{ key: string; order: string }[]>([{ key: "resultLevel", order: "asc" }]);
const groupBy = ref<{ key: string; order: string }[]>([]);
const headers = ref([
  { title: "Info", key: "message", groupable: true },
  { title: "EntityId", key: "affectedEntityId", groupable: true },
  { title: "Attribut", key: "affectedAttributeName", groupable: true },
  { title: "Level", key: "resultLevel", groupable: true },
  { title: "Status", key: "highlightLevel", groupable: true },
]);

watch(
  () => selectedResultTab.value,
  (newValue) => {
    submissionService.getResultBySubmissionIdAndLevel(submissions.value[newValue - 1].id, currentTask.value.showLevel).then((response) => {
      selectedResult.value = response.data;
    });
  }
);

const load = (task: Task) => {
  currentTask.value = task;

  submissionService.getSubmissionIdsByUserAndTask(userId.value, task.id).then((response) => {
    submissions.value = response.data;

    //select last tab
    selectedResultTab.value = submissions.value.length;

    //if first submission, load result for first submission
    if (submissions.value.length == 1) {
      submissionService.getResultBySubmissionIdAndLevel(submissions.value[submissions.value.length - 1].id, currentTask.value.showLevel).then((response) => {
        selectedResult.value = response.data;
      });
    }
  });
};

const showDiagramWithMistakes = () => {
  diagramStore.loadDiagram(submissions.value[selectedResultTab.value - 1].diagram as Diagram);
  dialogShowFullDiagram.value?.openDialog("");
};

const getHighlightLevelColor = (highlightLevel: string) => {
  if (highlightLevel == "INCORRECT") {
    return "red";
  } else if (highlightLevel == "CORRECT") {
    return "green";
  } else {
    return "gray";
  }
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

.table {
  width: 100%;

  tr {
    td {
      padding: 5px;
    }
  }
}
</style>
