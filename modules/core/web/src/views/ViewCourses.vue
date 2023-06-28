<!-- eslint-disable vue/valid-v-slot -->
<template>
  <BreadCrumb link="Alle Kurse"></BreadCrumb>
  <div class="container">
    <CoursesList ref="coursesList"></CoursesList>
    <v-btn id="createCourseBtn" color="primary-dark" @click="createCourse">Kurs erstellen</v-btn>
  </div>
  <DialogCreateCourse ref="dialogCreateCourse"></DialogCreateCourse>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import DialogCreateCourse from "../dialog/DialogCreateCourse.vue";
import CoursesList from "@/components/CoursesList.vue";
import BreadCrumb from "@/components/BreadCrumb.vue";

const dialogCreateCourse = ref<typeof DialogCreateCourse>();
const coursesList = ref<typeof CoursesList>();

const router = useRouter();

onMounted(() => {
  coursesList.value?.loadCourses();
});

const createCourse = () => {
  if (dialogCreateCourse.value) {
    dialogCreateCourse.value.openDialog().then((id: number) => {
      if (id != undefined) router.push("/course/" + id);
    });
  }
};
</script>

<style scoped lang="scss">
#createCourseBtn {
  margin: 0 auto;
  display: block;
  margin-top: 20px;
  margin-bottom: 20px;
}

.container {
  width: auto;
  margin: 20px 20px;
}
</style>
