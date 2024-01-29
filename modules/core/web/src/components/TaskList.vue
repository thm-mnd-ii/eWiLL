<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="container">
    <v-text-field v-model="search" class="search-bar" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details></v-text-field>
    <v-data-table :headers="headers" :items="listItems" item-value="name" class="elevation-1" density="default" height="480px" :search="search" @click:row="openTask">
      <template #item.passed="{ item }">
        <v-icon v-if="item.passed == false" icon="mdi-close-circle" color="error"></v-icon>
        <v-icon v-if="item.passed == true" icon="mdi-check-circle" color="success"></v-icon>
      </template>
      <template #item.task.eliability="{ item }">
        <v-chip v-if="item.task.eliability == 'MANDATORY'" color="red">Verpflichtend</v-chip>
        <v-chip v-if="item.task.eliability == 'OPTIONAL'" color="yellow">Optional</v-chip>
        <v-chip v-if="item.task.eliability == 'BONUS'" color="green">Bonus</v-chip>
      </template>
      <template #item.task.dueDate="{ item }">
        <TaskDateVChip :due-date-prop="item.task.dueDate"></TaskDateVChip>
      </template>
    </v-data-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import Task from "../model/task/Task";
import TaskAndPassed from "@/model/task/TaskAndPassed";
import taskService from "../services/task.service";
import TaskDateVChip from "./TaskDateVChip.vue";
import submissionService from "@/services/submission.service";

const authUserStore = useAuthUserStore();
const userId = ref(authUserStore.auth.user?.id);

const router = useRouter();
const search = ref("");
const saveCourseId = ref(0);

const headers = [
  { title: "Name", align: "start", key: "task.name" },
  { title: "Abgabeart", align: "start", key: "task.eliability" },
  { title: "Abgabeende", align: "start", key: "task.dueDate" },
  { title: "Bestanden", align: "start", key: "passed" },
];

const listItems = ref<TaskAndPassed[]>([]);

const loadTasks = (courseId: number) => {
  listItems.value = [];
  saveCourseId.value = courseId;
  taskService
    .getAllTasks(courseId)
    .then((data) => {
      data.forEach((task: Task) => {
        submissionService.getPassedByTaskAndUser(task.id, userId.value!).then((response) => {
          let item = {} as TaskAndPassed;
          item.task = task;
          item.passed = response.data;
          listItems.value.push(item);
        });
      });
    })
    .catch((error) => {
      console.log(error);
    });
};

const openTask = (row: any, item: any) => {
  router.push("/course/" + saveCourseId.value + "/task/" + item.item.task.id);
};

defineExpose({
  loadTasks,
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
