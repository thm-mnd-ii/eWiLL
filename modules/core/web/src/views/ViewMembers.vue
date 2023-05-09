<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="task">
    <v-card>
      <v-card-title class="task-header-title">
        <h3 class="headline mb-0">Teilnehmer</h3>
        <v-spacer></v-spacer>
        <v-btn variant="text" icon="mdi-keyboard-return" color="dark-gray"></v-btn>
      </v-card-title>
      <v-card-text>
        <p>{{ courseName }}</p>
        <br />
        <div class="align-items-center">
          <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label> {{ courseRole }} </v-chip>
          <v-spacer></v-spacer>
          <v-btn v-if="courseRole == 'OWNER' || courseRole == 'TUTOR'" variant="text" color="red">Alle Studenten entfernen</v-btn>
        </div>
      </v-card-text>
    </v-card>

    <div class="container">
      <v-text-field v-model="search" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details class="search-field"></v-text-field>
      <v-data-table :headers="headers" :items="testMembers" item-value="name" class="elevation-1" :search="search" density="default" height="480px">
        <template #item.role="{ item }">
          <v-select v-model="item.raw.role" item variant="plain" :items="['Student', 'Dozent', 'Owner']" class="select"></v-select>
        </template>
        <template #item.actions="{}">
          <v-btn icon="mdi-delete" color="red" variant="text"></v-btn>
        </template>
      </v-data-table>
    </div>

    <div class="task-main"></div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import courseService from "../services/course.service";
import CoursePL from "../model/course/CoursePL";

const route = useRoute();
const router = useRouter();
const authUserStore = useAuthUserStore();

const members = ref();

const course = ref<CoursePL>();
const courseName = ref("");
const courseId = ref(Number(route.params.courseId));
const userId = ref(authUserStore.auth.user?.id!);
const courseRole = ref("");

const search = ref("");
const headers = [
  { title: "Name", align: "start", key: "name" },
  { title: "Vorname", align: "start", key: "firstname" },
  { title: "E-Mail", align: "start", key: "email" },
  { title: "Rolle", align: "start", key: "role" },
  { title: "Aktion", align: "start", key: "actions", sortable: false },
];

const testMembers = [{ name: "Hönig", firstname: "Maximilian", email: "maximilian.hoenig@mnd.thm.de", role: "Student" }];

onMounted(() => {
  courseService.getUserRoleInCourse(userId.value, courseId.value).then((response) => {
    if (response == "NONE") {
      router.push("/course/" + route.params.courseId + "/signup");
    } else {
      courseRole.value = response;
      loadCourse();
    }
  });
});

const loadCourse = () => {
  courseService.getCourse(courseId.value).then((response) => {
    course.value = response.data;
    courseName.value = course.value.name;
  });
};
</script>

<style scoped>
.task {
  width: auto;
  margin: 20px 20px;
}

.task-header-title {
  display: flex;
  align-items: center;
}

.task-main {
  /* grid with left and right side */
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  grid-gap: 40px;
  margin: 20px 10px;
}

.align-items-center {
  display: flex;
  align-items: center;
}

.container {
  margin-top: 30px;
}

.search-field {
  margin-bottom: 20px;
}

.select {
  width: 90px;
}
</style>
