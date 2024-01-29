<!-- eslint-disable vue/valid-v-slot -->
<template>
  <DialogShowFeedback ref="dialogShowFeedback" />
  <DialogShowFullDiagram ref="dialogShowFullDiagram" />

  <div class="container">
    <v-text-field v-model="search" class="search-bar" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details></v-text-field>
    <v-row>
      <v-checkbox v-model="onlyLatestSubmission" label="Nur die letzte Abgabe eines Studenten zeigen" @change="filterList"></v-checkbox>
    </v-row>
    <v-data-table :headers="headers" :items="listItems" class="elevation-1" density="default" height="480px" :search="search">
      <template #item.submission.diagram="{ item }">
        <v-btn append-icon="mdi-presentation" @click="openDiagram(item.submission.diagram)">Preview</v-btn>
      </template>
      <template #item.result.score="{ item }">
        <v-chip b-tooltip.hover class="fixed-size-chip" title="Zeige komplettes Feedback" :style="{ background: colors(item), color: 'black' }" @click="openFeedback(item.result.comments, item.submission.attempt)"> {{ parseFloat(item.result.score).toFixed(2) }}</v-chip>
      </template>
      <template #item.result.correct="{ item }">
        <v-icon v-if="item.result.correct == true" icon="mdi-check-circle" color="success"></v-icon>
        <v-icon v-if="item.result.correct == false" icon="mdi-close-circle" color="red"></v-icon>
      </template>
    </v-data-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useDiagramStore } from "@/stores/diagramStore";

import submissionService from "@/services/submission.service";
import courseService from "@/services/course.service";
import taskService from "@/services/task.service";

import Task from "@/model/task/Task";
import Member from "@/model/course/Member";
import SubmissionListItem from "@/model/submission/SubmissionsListItem";
import Diagram from "@/model/diagram/Diagram";
import Submission from "@/model/submission/Submission";

import DialogShowFullDiagram from "@/dialog/DialogShowFullDiagram.vue";
import DialogShowFeedback from "@/dialog/DialogShowFeedback.vue";

const dialogShowFullDiagram = ref<typeof DialogShowFullDiagram>();
const dialogShowFeedback = ref<typeof DialogShowFeedback>();
const diagramStore = useDiagramStore();

const search = ref("");
const onlyLatestSubmission = ref();
const task = ref<Task>();

const submissions = ref<Submission[]>([]);
const students = ref<Map<number, Member>>();
const listItems = ref<SubmissionListItem[]>([]);

const headers = [
  { title: "Vorname", align: "start", key: "member.user.firstName" },
  { title: "Name", align: "start", key: "member.user.lastName" },
  { title: "Diagram", align: "start", key: "submission.diagram" },
  { title: "Versuch", align: "start", key: "submission.attempt" },
  { title: "Datum", align: "start", key: "submission.date" },
  { title: "Score", align: "start", key: "result.score" },
  { title: "Korrekt", align: "start", key: "result.correct" },
];

const loadSubmissions = (taskid: number) => {
  submissionService
    .getSubmissionsByTask(taskid)
    .then((response) => {
      submissions.value = response.data;
      taskService.getTask(taskid).then((response) => {
        task.value = response;
        loadUsers();
      });
    })
    .catch((error) => {
      console.log(error);
    });
};

const loadLatestSubmissions = () => {
  submissionService.getLatestSubmissionsByTask(task.value!.id).then((response) => {
    submissions.value = response.data;
    combineSubmissionsWithStudents();
  });
};

const colors = (item: any) => {
  const score = item.result.score;

  if (isNaN(score) || score < 50) {
    return "#FF6666";
  } else if (score >= 50 && score < 100) {
    return "#FFA500";
  } else {
    return "#B2FF66";
  }
};

const loadUsers = () => {
  courseService.getCourseMembersAsMap(task.value!.courseId).then((map) => {
    students.value = map;
    combineSubmissionsWithStudents();
  });
};

const combineSubmissionsWithStudents = () => {
  listItems.value = [];
  submissions.value.forEach((submission: Submission) => {
    let item = {} as SubmissionListItem;
    item.submission = submission;
    item.member = students.value!.get(submission.userId)!;

    submissionService.getResultBySubmissionIdAndLevel(submission.id, "INFO").then((response) => {
      item.result = response.data;
      listItems.value.push(item);
    });
  });
};

const filterList = () => {
  if (onlyLatestSubmission.value) {
    loadLatestSubmissions();
  } else {
    loadSubmissions(task.value!.id);
  }
};

const openDiagram = (diagram: Diagram) => {
  console.log(diagram);
  diagramStore.loadDiagram(diagram);
  dialogShowFullDiagram.value?.openDialog("");
};
const openFeedback = (comments: any, versuch: Number) => {
  var message: string[] = [];
  const comms = JSON.stringify(comments);
  const obj = JSON.parse(comms);
  for (const [index, item] of obj.entries()) {
    message.push(
      `Hinweis (${index + 1}) : <br>` +
        `+ feedbackLevel: ${item.feedbackLevel}<br>` +
        `+ message: ${item.message}<br>` +
        `+ resultMessageType: ${item.resultMessageType}<br>` +
        `+ affectedEntityId: ${item.affectedEntityId}<br>` +
        `+ affectedEntityName: ${item.affectedEntityName}<br>` +
        `+ affectedAttributeName: ${item.affectedAttributeName}<br>` +
        `+ connectedToId: ${item.connectedToId}<br>` +
        `+ statusLevel: ${item.statusLevel} <br> <br>`
    );
  }
  const dialogContent = `${message.join("")}`;
  dialogShowFeedback.value?.openDialog("Feedback", dialogContent, versuch);
};

defineExpose({
  loadSubmissions,
});
</script>

<style scoped lang="scss">
.container {
  cursor: pointer;
}
.fixed-size-chip {
  width: 75px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: flex;
  justify-content: center;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}
</style>
