<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="container">
    <v-text-field v-model="search" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details></v-text-field>
    <v-row>
      <v-checkbox v-model="checkboxActive" label="Nur aktive Kurse anzeigen" @change="onCheckboxChange"></v-checkbox>
      <v-checkbox v-model="checkboxFriedberg" label="Friedberg" @change="onCheckboxChange"></v-checkbox>
      <v-checkbox v-model="checkboxGießen" label="Gießen" @change="onCheckboxChange"></v-checkbox>
      <v-checkbox v-model="checkboxParticipation" label="Teilnahme" @change="onCheckboxChange"></v-checkbox>
    </v-row>
    <v-data-table :headers="headers" :items="displayedCourses" item-value="name" class="elevation-1" :search="search" density="default" height="480px" @click:row="openCourseOrSignUpView">
      <template #item.active="{ item }">
        <v-icon v-if="item.raw.active == 0" icon="mdi-close-circle" color="#DD0000"></v-icon>
        <v-icon v-if="item.raw.active == 1" icon="mdi-check-circle" color="#81BA24"></v-icon>
      </template>
      <template #item.participation="{ item }">
        <v-icon v-if="item.raw.participation == 1" icon="mdi-check-bold" color="#81BA24"></v-icon>
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

// Courses lists
const displayedCourses = ref<Course[]>([]);
const allCourses = ref<Course[]>([]);

// Checkboxes
const checkboxActive = ref(false);
const checkboxFriedberg = ref(false);
const checkboxGießen = ref(false);
const checkboxParticipation = ref(false);

onMounted(() => {
  let userId = authUserStore.auth.user?.id;
  if (userId != undefined) {
    allCourses.value = courseService.getAllCourses(userId);
    displayedCourses.value = allCourses.value;
    buildSubCourseLists();
  } else {
    console.log("userId is undefined");
  }
});

const buildSubCourseLists = () => {
  let tmpCoursesActive: Course[] = [];
  let tmpCoursesParticipation: Course[] = [];
  let tmpCoursesFriedberg: Course[] = [];
  let tmpCoursesGießen: Course[] = [];

  allCourses.value.forEach((course) => {
    if (course.active == 1) tmpCoursesActive.push(course);
    if (course.participation == 1) tmpCoursesParticipation.push(course);
    if (course.location == "Friedberg") tmpCoursesFriedberg.push(course);
    if (course.location == "Gießen") tmpCoursesGießen.push(course);
  });
};

const onCheckboxChange = () => {
  let filteredList: Course[] = allCourses.value;
  if (checkboxActive.value) filteredList = filteredList.filter((course) => course.active == 1);
  if (checkboxFriedberg.value) filteredList = filteredList.filter((course) => course.location == "Friedberg");
  if (checkboxGießen.value) filteredList = filteredList.filter((course) => course.location == "Gießen");
  if (checkboxParticipation.value) filteredList = filteredList.filter((course) => course.participation == 1);
  displayedCourses.value = filteredList;
};

const createCourse = () => {
  console.log("create course");
};

const openCourseOrSignUpView = (row: any, item: any) => {
  console.log(item.item.raw.id);
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
