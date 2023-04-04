<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="container">
    <v-text-field v-model="search" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details></v-text-field>
    <v-row>
      <v-checkbox v-model="checkboxActive" label="Nur aktive Kurse anzeigen" @change="filterCourseList"></v-checkbox>
      <v-checkbox v-model="checkboxFriedberg" label="Friedberg" @change="filterCourseList" @click="checkboxGießen = false"></v-checkbox>
      <v-checkbox v-model="checkboxGießen" label="Gießen" @change="filterCourseList" @click="checkboxFriedberg = false"></v-checkbox>
      <v-checkbox v-model="checkboxParticipation" label="Teilnahme" @change="filterCourseList"></v-checkbox>
    </v-row>
    <v-data-table :headers="headers" :items="displayedCourses" item-value="name" class="elevation-1" :search="search" density="default" height="480px" @click:row="openCourseOrSignUp">
      <template #item.active="{ item }">
        <v-icon v-if="item.raw.active == false" icon="mdi-close-circle" color="#DD0000"></v-icon>
        <v-icon v-if="item.raw.active == true" icon="mdi-check-circle" color="#81BA24"></v-icon>
      </template>
      <template #item.participation="{ item }">
        <v-icon v-if="item.raw.participation == true" icon="mdi-check-bold" color="#81BA24"></v-icon>
      </template>
    </v-data-table>
    <v-btn id="createCourseBtn" @click="createCourse">Kurs erstellen</v-btn>
  </div>
  <DialogCreateCourse ref="dialogCreateCourse"></DialogCreateCourse>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import Course from "../model/course/Course";
import courseService from "../services/course.service";
import { useAuthUserStore } from "../stores/authUserStore";
import { useRouter } from "vue-router";
import DialogCreateCourse from "../dialog/DialogCreateCourse.vue";

const dialogCreateCourse = ref<typeof DialogCreateCourse>();

const router = useRouter();
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
    courseService
      .getAllCourses(userId)
      .then((data) => {
        allCourses.value = data;
        displayedCourses.value = allCourses.value;
      })
      .catch((error) => {
        console.log(error);
      });
  } else {
    console.log("userId is undefined");
  }
});

const filterCourseList = () => {
  let filteredList: Course[] = allCourses.value;
  if (checkboxActive.value) filteredList = filteredList.filter((course) => course.active == true);
  if (checkboxFriedberg.value) filteredList = filteredList.filter((course) => course.location == "Friedberg");
  if (checkboxGießen.value) filteredList = filteredList.filter((course) => course.location == "Gießen");
  if (checkboxParticipation.value) filteredList = filteredList.filter((course) => course.participation == true);
  displayedCourses.value = filteredList;
};

const createCourse = () => {
  if (dialogCreateCourse.value) {
    dialogCreateCourse.value.openDialog().then((id: number) => {
      if (id != undefined) router.push("/course/" + id);
    });
  }
};

const openCourseOrSignUp = (row: any, item: any) => {
  if (item.item.raw.participation == false) router.push("/course/" + item.item.raw.id + "/signup");
  else router.push("/course/" + item.item.raw.id);
};
</script>

<style scoped lang="scss">
.container {
  margin-top: 30px;
  position: relative;
  left: 500px;
  width: 700px;
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
