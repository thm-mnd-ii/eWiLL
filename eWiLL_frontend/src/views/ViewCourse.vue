<template>
  <div class="course">
    <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>
    <v-card>
      <v-card-title class="align-items-center">
        <h3 class="headline mb-0">{{ course?.name }}</h3>
        <v-spacer></v-spacer>
        <v-btn variant="text" icon="mdi-account-group" color="dark-gray"></v-btn>
        <v-btn variant="text" icon="mdi-logout-variant" color="dark-gray" @click="leaveCourse"></v-btn>
        <v-btn variant="text" icon="mdi-cog" color="dark-gray"></v-btn>
      </v-card-title>
      <v-card-text>
        <p>{{ course?.description }}</p>
        <br />
        <div class="align-items-center">
          <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label> {{ courseRole }} </v-chip>
          <v-spacer></v-spacer>
          <v-btn v-if="courseRole == 'OWNER' || courseRole == 'TUTOR'" variant="text">Aufgabe erstellen</v-btn>
        </div>
      </v-card-text>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import DialogConfirmVue from "../dialog/DialogConfirm.vue";
import Course from "../model/course/Course";
import CoursePL from "../model/course/CoursePL";
import courseService from "../services/course.service";

const route = useRoute();
const authUserStore = useAuthUserStore();
const dialogConfirm = ref<typeof DialogConfirmVue>();
const course = ref<CoursePL>();
const courseId = ref(Number(route.params.id));
const userId = ref(authUserStore.auth.user?.id);
const courseRole = ref("");

onMounted(() => {
  courseService.getCourse(courseId.value).then((response) => {
    course.value = response.data;
  });
  if (userId.value != undefined) {
    courseService.getUserRoleInCourse(userId.value, courseId.value).then((response) => (courseRole.value = response));
  }
});

const leaveCourse = () => {
  console.log(dialogConfirm.value);
  if (dialogConfirm.value) {
    dialogConfirm.value.openDialog(`Verlasse Kurs: ${course.value?.name}`, "Willst du den Kurs wirklich verlassen?").then((result: boolean) => {
      console.log("yeah buddy");
    });
  }
};
</script>

<style scoped>
.course {
  width: auto;
  margin: 20px 20px;
}
.align-items-center {
  display: flex;
  align-items: center;
}
</style>
