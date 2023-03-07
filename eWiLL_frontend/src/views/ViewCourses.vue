<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="container">
    <v-text-field v-model="search" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details></v-text-field>
    <v-row>
      <v-checkbox label="Nur aktive Kurse anzeigen"></v-checkbox>
      <v-checkbox label="Friedberg"></v-checkbox>
      <v-checkbox label="Gießen"></v-checkbox>
      <v-checkbox label="Teilnahme"></v-checkbox>
    </v-row>
    <v-data-table :headers="headers" :items="courses" item-value="name" class="elevation-1" :search="search">
      <template #item.active="{ item }">
        <v-icon v-if="item.raw.active == 0" icon="mdi-close-circle" color="#DD0000"></v-icon>
        <v-icon v-if="item.raw.active == 1" icon="mdi-check-circle" color="#81BA24"></v-icon>
      </template>
      <template #item.participation="{ item }">
        <v-icon v-if="item.raw.active == 1" icon="mdi-check-bold" color="#81BA24"></v-icon>
      </template>
    </v-data-table>
    <v-btn id="createCourseBtn" @click="createCourse">Kurs erstellen</v-btn>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import Course from "../model/course/Course";
import courseService from "../services/course.service";
import { useAuthUserStore } from "../stores/authUserStore";

const authUserStore = useAuthUserStore();

const search = ref("");
const headers = [
  { title: "Semester", align: "start", key: "semester" },
  { title: "Aktiv", align: "start", key: "active" },
  { title: "Kursname", align: "start", key: "name" },
  { title: "Standort", align: "start", key: "location" },
  { title: "Teilnahme", align: "start", key: "participation" },
];
const courses = ref<Course[]>([]);

onMounted(() => {
  let userId = authUserStore.auth.user?.id;
  if (userId != undefined) {
    courses.value = courseService.getAllCourses(userId);
  } else {
    console.log("userId is undefined");
  }
});

const createCourse = () => {
  console.log("create course");
};
</script>

<style scoped lang="scss">
.container {
  margin-top: 30px;
  position: relative;
  left: 500px;
  width: 700px;
}

#createCourseBtn {
  color: #ffffff;
  margin-top: 20px;
  background-color: #81ba24;
  position: absolute;
  left: 38%;
}
</style>
