<template>
  <div class="course">
    <v-card>
      <v-card-title class="align-items-center">
        <h3 class="headline mb-0">Abgaben von {{ task?.name }}</h3>
        <v-spacer></v-spacer>
      </v-card-title>
      <v-card-text>
        <p>{{ task?.description }}</p>
        <br />
        <div class="align-items-center">
          <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label> {{ courseRole }} </v-chip>
          <v-spacer></v-spacer>
          <v-chip v-if="task?.maxSubmissions != 999" class="margin-right-5px">Versuche: {{ task?.maxSubmissions }}</v-chip>
          <v-chip v-if="task?.maxSubmissions == 999" class="margin-right-5px">Versuche: unbegrenzt</v-chip>
          <v-chip v-if="task?.eliability == 'BONUS'" color="green">Bonus</v-chip>
          <v-chip v-if="task?.eliability == 'MANDATORY'" color="red">Verpflichtend</v-chip>
          <v-chip v-if="task?.eliability == 'OPTIONAL'" color="yellow">Optional</v-chip>
        </div>
      </v-card-text>
    </v-card>
    <div class="task_list">
      <SubmissionsList ref="submissionsList"></SubmissionsList>
    </div>
  </div>
  <DialogCreateCourse ref="dialogCreateCourse"></DialogCreateCourse>
  <DialogEditTask ref="dialogCreateTask"></DialogEditTask>
  <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import { useRouter } from "vue-router";

import SubmissionsList from "@/components/SubmissionsList.vue";
import Task from "../model/task/Task";

import taskService from "../services/task.service";
import courseService from "../services/course.service";

import DialogConfirmVue from "../dialog/DialogConfirm.vue";
import DialogCreateCourse from "@/dialog/DialogCreateCourse.vue";
import DialogEditTask from "@/dialog/DialogEditTask.vue";

const route = useRoute();
const router = useRouter();
const authUserStore = useAuthUserStore();

const submissionsList = ref<typeof SubmissionsList>();
const courseId = ref(Number(route.params.courseId));
const taskId = ref(Number(route.params.taskId));
const userId = ref(authUserStore.auth.user?.id);
const courseRole = ref("");

const dialogCreateCourse = ref<typeof DialogCreateCourse>();
const dialogCreateTask = ref<typeof DialogEditTask>();
const dialogConfirm = ref<typeof DialogConfirmVue>();

const task = ref<Task>();

onMounted(() => {
  courseService.getUserRoleInCourse(userId.value!, courseId.value).then((response) => {
    if (response == "NONE") {
      router.push(route.path + "/signup");
    } else {
      courseRole.value = response;
      submissionsList.value!.loadSubmissions(taskId.value);
      taskService.getTask(taskId.value).then((response) => {
        task.value = response;
      });
    }
  });
});
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
