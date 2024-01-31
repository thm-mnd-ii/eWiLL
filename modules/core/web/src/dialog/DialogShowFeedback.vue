<template>
  <v-dialog v-model="deleteDialog" width="100vw" height="100vh">
    <v-card class="modelPreview" variant="outlined" width="100%" height="100%">
      <div class="message-container">
        <TaskSubmissionsResultsTabs ref="taskSubmissionsResultsTabs" class="full-height"></TaskSubmissionsResultsTabs>
      </div>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" background-color="teal-darken-3" @click="_cancel">Schließen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from "vue";
import TaskSubmissionsResultsTabs from "@/components/TaskSubmissionsResultsTabs.vue";
import { useAuthUserStore } from "../stores/authUserStore";
import evaluationService from "@/services/evaluation.service";
import { useRoute } from "vue-router";
import Task from "../model/task/Task";
import taskService from "../services/task.service";

const task = ref<Task>({} as Task);
const route = useRoute();
const taskId = ref(Number(route.params.taskId));
const taskSubmissionsResultsTabs = ref<typeof TaskSubmissionsResultsTabs>();
const authUserStore = useAuthUserStore();
const userId = ref(authUserStore.auth.user?.id!);
const deleteDialog = ref<boolean>(false);
const Title = ref<string>("");
const Message = ref<string | undefined>(undefined);
// Promises
const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);
const submissionCount = ref(1);

onMounted(() => {
  init();
});

const init = () => {
  loadElements();
};

const loadElements = async () => {
  await loadTask();
  await loadSubmissions();
};

const loadTask = () => {
  taskService.getTask(taskId.value).then((response) => {
    task.value = response;
    console;
  });
};

const loadSubmissions = (selectedTaskIndex?: Number) => {
  evaluationService.getSubmissionIdsByUserAndTask(userId.value, taskId.value).then((response) => {
    const submissionIds = response.data;
    submissionCount.value = submissionIds.length;
    taskSubmissionsResultsTabs.value?.load(task.value, selectedTaskIndex);
  });
};
const openDialog = async (title: string, message: string, selectedTaskIndex?: number) => {
  try {
    await loadElements();
    await loadSubmissions(selectedTaskIndex);

    // Force update
    await nextTick();
  } catch (error) {
    console.error("Failed to load data for dialog:", error);
  }
  Title.value = title;
  Message.value = message;
  deleteDialog.value = true;

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _cancel = () => {
  deleteDialog.value = false;
  resolvePromise.value(false);
};

defineExpose({
  openDialog,
});
</script>

<style scoped>
.modelPreview {
  position: relative;
  width: 100%;
  height: 100%;
  background-color: white;
  z-index: 0;
  overflow: auto;
  display: flex;
  flex-direction: column;
}
.message-container {
  flex-grow: 1;
  display: flex;
}
</style>
