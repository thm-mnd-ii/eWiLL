<!-- eslint-disable vue/valid-v-slot -->
<template>
  <DialogShowFullDiagram ref="dialogShowFullDiagram" />

  <div class="container">
    <v-text-field v-model="search" class="search-bar" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details></v-text-field>
    <v-row>
      <v-checkbox v-model="onlyLatestSubmission" label="Nur die letzte Abgabe eines Studenten zeigen" @change="filterList"></v-checkbox>
    </v-row>
    <v-data-table :headers="headers" :items="listItems" class="elevation-1" density="default" height="480px" :search="search">
      <template #item.submission.diagram="{ item }">
        <v-btn append-icon="mdi-presentation" @click="openDiagram(item.value.submission.diagram)">Preview</v-btn>
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

const dialogShowFullDiagram = ref<typeof DialogShowFullDiagram>();

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
    listItems.value.push(item);
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

defineExpose({
  loadSubmissions,
});
</script>

<style scoped lang="scss">
.container {
  position: flex;

  cursor: pointer;
}

.search-bar {
  margin-bottom: 20px;
}
</style>
