<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="container">
    <v-data-table :headers="headers" :items="allTasks" item-value="name" class="elevation-1" density="default" height="480px" @click:row="openTask">
      <template #item.status="{ item }">
        <v-icon v-if="item.raw.status == false" icon="mdi-close-circle" color="#DD0000"></v-icon>
        <v-icon v-if="item.raw.status == true" icon="mdi-check-circle" color="#81BA24"></v-icon>
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

const headers = [
  { title: "Name", align: "start", key: "name" },
  { title: "Status", align: "start", key: "status" },
  { title: "Abgabeende", align: "end", key: "dueDate" },
];

const allTasks = ref<Task[]>([]);

const loadTasks = (courseId: number) => {
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
  if (item.item.raw.participation == false) router.push("/course/" + item.item.raw.id + "/signup");
  else router.push("/course/" + item.item.raw.id);
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

#createCourseBtn {
  color: #ffffff;
  margin-top: 20px;
  background-color: #81ba24;
  position: absolute;
  left: 38%;
}
</style>
