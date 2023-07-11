<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="container">
    <v-text-field v-model="search" class="search-bar" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details></v-text-field>
    <v-data-table :headers="headers" :items="allTasks" item-value="name" class="elevation-1" density="default" height="480px" :search="search" @click:row="openTask">
      <template #item.status="{ item }">
        <v-icon v-if="item.raw.status == false" icon="mdi-close-circle" color="#DD0000"></v-icon>
        <v-icon v-if="item.raw.status == true" icon="mdi-check-circle" color="#81BA24"></v-icon>
      </template>
      <template #item.eliability="{ item }">
        <v-chip v-if="item.raw.eliability == 'MANDATORY'" color="red">Verpflichtend</v-chip>
        <v-chip v-if="item.raw.eliability == 'OPTIONAL'" color="yellow">Optional</v-chip>
        <v-chip v-if="item.raw.eliability == 'BONUS'" color="green">Bonus</v-chip>
      </template>
    </v-data-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import Task from "../model/task/Task";
import taskService from "../services/task.service";

const router = useRouter();
const search = ref("");
const saveCourseId = ref(0);

const headers = [
  { title: "Name", align: "start", key: "name" },
  { title: "Abgabeart", align: "start", key: "eliability" },
  { title: "Abgabeende", align: "start", key: "dueDate" },
  { title: "Bestanden", align: "start", key: "status" },
];

const allTasks = ref<Task[]>([]);

const loadTasks = (courseId: number) => {
  saveCourseId.value = courseId;
  taskService
    .getAllTasks(courseId)
    .then((data) => {
      allTasks.value = data;
    })
    .catch((error) => {
      console.log(error);
    });
};

const openTask = (row: any, item: any) => {
  router.push("/course/" + saveCourseId.value + "/task/" + item.item.raw.id);
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

#createCourseBtn {
  color: #ffffff;
  margin-top: 20px;
  background-color: #81ba24;
  position: absolute;
  left: 38%;
}
</style>
