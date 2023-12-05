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
            <v-data-table :group-by="groupBy" :headers="headers" :items="selectedResult.comments" :sort-by="sortBy" class="elevation-1" item-value="name"  @click:row="onRowClick($event,selectedResult.comments)">
              <template #item.highlightLevel="{ item }" >
                <!-- <v-chip :color="getHighlightLevelColor(item.value.highlightLevel)"> -->
                <v-icon v-if="item.value.highlightLevel == 'SUGGESTION'" size="large" :color="getHighlightLevelColor(item.value.highlightLevel)">mdi-information</v-icon>
                <v-icon v-if="item.value.highlightLevel == 'INCORRECT'" size="large" :color="getHighlightLevelColor(item.value.highlightLevel)">mdi-close-circle</v-icon>
                <v-icon v-if="item.value.highlightLevel == 'CORRECT'" size="large" :color="getHighlightLevelColor(item.value.highlightLevel)">mdi-check-circle</v-icon>
                <!-- {{ item.value.highlightLevel }} -->
                <!-- </v-chip> -->
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
import { defineEmits } from 'vue';
import FeedbackLevel from "@/enums/FeedbackLevel";

const authUserStore = useAuthUserStore();
const diagramStore = useDiagramStore();
const userId = ref(authUserStore.auth.user?.id!);

const currentTask = ref<Task>({} as Task);

const selectedResultTab = ref<number>(1);
const dialogShowFullDiagram = ref<typeof DialogShowFullDiagram>();

const submissions = ref<Submission[]>([] as Submission[]);
const selectedResult = ref<Result>({} as Result);

const sortBy = ref<{ key: string; order: string }[]>([{ key: "feedbackLevel", order: "asc" }]);
const groupBy = ref<{ key: string; order: string }[]>([]);
const headers = ref([
  { title: "Status", key: "feedbackLevel", groupable: true },
  { title: "Info", key: "message", groupable: true },
  { title: "Entity", key: "affectedEntityName", groupable: true },
  { title: "Attribut", key: "affectedAttributeName", groupable: true },
  { title: "Level", key: "statusLevel", groupable: true },
  { title: "Info Type", key: "resultMessageType", groupable: true },
]);

const emit = defineEmits(['highlight-entity']);

watch(
  () => selectedResultTab.value,
  (newValue) => {
    submissionService.getResultBySubmissionIdAndLevel(submissions.value[newValue - 1].id, currentTask.value.showLevel).then((response) => {
      selectedResult.value = response.data;
    });
  }
);

const load = (task: Task, selectedSubmissionIndex?: number) => {
  console.log("load called ");

  currentTask.value = task;

  submissionService.getSubmissionIdsByUserAndTask(userId.value, task.id).then((response) => {
    submissions.value = response.data;
    console.log("selectedResult.value.comments" + JSON.stringify(selectedResult.value.comments));
    console.log("load called ");

    //select last tab
    selectedResultTab.value = selectedSubmissionIndex !== undefined ? selectedSubmissionIndex : submissions.value.length;

    //if first submission, load result for first submission
    if (submissions.value.length == 1) {
      submissionService.getResultBySubmissionIdAndLevel(submissions.value[submissions.value.length - 1].id, currentTask.value.showLevel).then((response) => {
        selectedResult.value = response.data;
      });
    }
  });
};

const loadLatestSubmissions = (task: Task) => {
    console.log("loadLatest called");
currentTask.value = task;
    submissionService.getLatestSubmissionsByTask(currentTask.value.id).then((response) => {
        const latestSubmissions = response.data;
        if (latestSubmissions && latestSubmissions.length > 0) {
            submissions.value = [latestSubmissions[0]];
            selectedResultTab.value = 1; // Since there's only one submission, the tab index is 1

            submissionService.getResultBySubmissionIdAndLevel(latestSubmissions[0].id, currentTask.value.showLevel).then((response) => {
                selectedResult.value = response.data;
            });
        } else {
            console.log("No submissions found for the latest task");
            // Handle scenario where no submissions are found
        }
    });
};


const showDiagramWithMistakes = () => {
  diagramStore.loadDiagram(submissions.value[selectedResultTab.value - 1].diagram as Diagram);
  console.log("showDiagramWithMistakes called ",diagramStore.loadDiagram(submissions.value[selectedResultTab.value - 1].diagram as Diagram));
  dialogShowFullDiagram.value?.openDialog("");
};

const getHighlightLevelColor = (highlightLevel: string) => {
  if (highlightLevel == "INCORRECT") {
    return "red";
  } else if (highlightLevel == "CORRECT") {
    return "green";
  } else if (highlightLevel == "SUGGESTION") {
    return "blue";
  } else {
    return "gray";
  }
};

const onRowClick = (event:any, comments:any) => {
  diagramStore.loadDiagram(submissions.value[selectedResultTab.value - 1].diagram as Diagram);
  let rowIndex = event.target.closest('tr').sectionRowIndex;
  console.log("Row index: " + rowIndex);
  console.log("selected Comment ", JSON.stringify(comments[rowIndex]));
  const entityId = Number(comments[rowIndex].affectedEntityId);
  const feedbackLevel = JSON.stringify(comments[rowIndex].feedbackLevel);
  if (entityId == null || feedbackLevel == FeedbackLevel.BASIC) { // add case for -1, feedbackLevel are all info ?
    console.error("Entity ID is null or undefined");
    return;
  } else {
    console.log("affected bezbez", entityId);
    emit('highlight-entity', entityId);
  }


 /**const diagramEntities = diagramStore.diagram.entities;
 console.log("the diagram:= " + JSON.stringify(diagramStore.diagram));
console.log("the entities in the diagram: " + JSON.stringify(diagramEntities));
console.log("the comment: " + JSON.stringify(comments[rowIndex]));
console.log("the comment id: " + JSON.stringify(comments[rowIndex].affectedEntityId));
  const matchingEntity = diagramEntities.find(entity => entity.id === comments[rowIndex].affectedEntityId);
console.log("the matching entity: " + JSON.stringify(matchingEntity));
  if (matchingEntity) {
    console.log("matching", matchingEntity, "affected", entityId);
    //dialogShowFullDiagram.value?.openDialog(matchingEntity);
  } else {
    alert('No matching entity found');
  }*/
};


defineExpose({
  load,
});
</script>

<style scoped>
.task-trials-tabs {
  margin: 0;
  width: 100%;
  height: 100%;
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
