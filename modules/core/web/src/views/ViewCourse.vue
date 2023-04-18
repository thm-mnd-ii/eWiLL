<template>
  <div class="course">
    <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>
    <v-card>
      <v-card-title class="align-items-center">
        <h3 class="headline mb-0">{{ course?.name }}</h3>
        <v-spacer></v-spacer>
        <v-btn variant="text" icon="mdi-account-group" color="dark-gray"></v-btn>
        <v-btn variant="text" icon="mdi-logout-variant" color="dark-gray" @click="leaveCourse"></v-btn>
        <v-btn variant="text" icon="mdi-cog" color="dark-gray" @click="editCourse"></v-btn>
      </v-card-title>
      <v-card-text>
        <p>{{ course?.description }}</p>
        <br />
        <div class="align-items-center">
          <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label> {{ courseRole }} </v-chip>
          <v-spacer></v-spacer>
          <v-btn v-if="courseRole == 'OWNER' || courseRole == 'TUTOR'" variant="text" @click="createTask">Aufgabe erstellen</v-btn>
        </div>
      </v-card-text>
    </v-card>
    <div class="task_list"><TaskList ref="taskList"></TaskList></div>
  </div>
  <DialogCreateCourse ref="dialogCreateCourse"></DialogCreateCourse>
  <DialogEditTask ref="dialogCreateTask"></DialogEditTask>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import { useRouter } from "vue-router";
import DialogConfirmVue from "../dialog/DialogConfirm.vue";
import TaskList from "../components/TaskList.vue";
import CoursePL from "../model/course/CoursePL";
import courseService from "../services/course.service";
import DialogCreateCourse from "@/dialog/DialogCreateCourse.vue";
import DialogEditTask from "@/dialog/DialogEditTask.vue";

const route = useRoute();
const router = useRouter();
const authUserStore = useAuthUserStore();
const dialogConfirm = ref<typeof DialogConfirmVue>();
const taskList = ref<typeof TaskList>();
const course = ref<CoursePL>();
const courseId = ref(Number(route.params.id));
const userId = ref(authUserStore.auth.user?.id);
const courseRole = ref("");
const dialogCreateCourse = ref<typeof DialogCreateCourse>();
const dialogCreateTask = ref<typeof DialogEditTask>();

onMounted(() => {
  courseService.getCourse(courseId.value).then((response) => {
    course.value = response.data;
  });
  if (userId.value != undefined) {
    courseService.getUserRoleInCourse(userId.value, courseId.value).then((response) => (courseRole.value = response));
  }

  if (taskList.value) {
    taskList.value.loadTasks(courseId.value);
  }
});

const leaveCourse = () => {
  if (dialogConfirm.value) {
    dialogConfirm.value.openDialog(`Verlasse Kurs: ${course.value?.name}`, "Willst du den Kurs wirklich verlassen?").then((result: boolean) => {
      if (result) {
        courseService
          .leaveCourse(courseId.value, userId.value)
          .then((response) => {
            // console.log(response);
            router.push("/");
          })
          .catch((error) => {
            console.log(error);
          });
      }
    });
  }
};

const editCourse = () => {
  if (dialogCreateCourse.value) {
    dialogCreateCourse.value.openDialog(courseId.value).then((id: number) => {
      // console.log("success");
    });
  }
};

const createTask = () => {
  if (dialogCreateTask.value) {
    dialogCreateTask.value.openDialog().then((created: boolean) => {
      console.log(created);
    });
  }
};
</script>

<style scoped>
.course {
  width: auto;
  margin: 20px 20px;
}

.task_list {
  margin-top: 30px;
}
.align-items-center {
  display: flex;
  align-items: center;
}
</style>
