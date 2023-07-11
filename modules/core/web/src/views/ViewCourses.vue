<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="container">
    <CoursesList ref="coursesList"></CoursesList>
    <v-btn v-if="isAdmin" id="createCourseBtn" color="primary-dark" @click="createCourse">Kurs erstellen</v-btn>
  </div>
  <DialogCreateCourse ref="dialogCreateCourse"></DialogCreateCourse>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import DialogCreateCourse from "../dialog/DialogCreateCourse.vue";
import CoursesList from "@/components/CoursesList.vue";
import { useAuthUserStore } from "../stores/authUserStore";
import GlobalRoles from "@/enums/GlobalRoles";

const authUserStore = useAuthUserStore();
const router = useRouter();

const dialogCreateCourse = ref<typeof DialogCreateCourse>();
const coursesList = ref<typeof CoursesList>();

const isAdmin = ref(false);

onMounted(() => {
  coursesList.value?.loadCourses();

  isAdmin.value = authUserStore.user?.roles.includes(GlobalRoles.ROLE_ADMIN)!;
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
