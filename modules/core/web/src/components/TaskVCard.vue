<template>
  <DialogEditTask ref="dialogEditTask" />

  <v-card>
    <v-card-title class="task-header-title">
      <h3 class="headline mb-0">{{ task.name }}</h3>
      <v-spacer></v-spacer>
      <v-btn v-if="props.courseRole != 'STUDENT'" variant="text" icon="mdi-cog" color="dark-gray" @click="openSettings"></v-btn>
    </v-card-title>
    <v-card-text>
      <p>{{ task.description }}</p>
      <br />
      <div class="align-items-center">
        <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label>
          {{ props.courseRole }}
        </v-chip>
        <v-spacer></v-spacer>
        <TaskDateVChip ref="taskDateVChip" class="margin-right-5px" :due-date-prop="task.dueDate"></TaskDateVChip>
        <v-chip v-if="task.maxSubmissions != 999" class="margin-right-5px">Versuche: {{ task.maxSubmissions }}</v-chip>
        <v-chip v-if="task.maxSubmissions == 999" class="margin-right-5px">Versuche: unbegrenzt</v-chip>
        <v-chip v-if="task.eliability == 'BONUS'" color="green">Bonus</v-chip>
        <v-chip v-if="task.eliability == 'MANDATORY'" color="red">Verpflichtend</v-chip>
        <v-chip v-if="task.eliability == 'OPTIONAL'" color="yellow">Optional</v-chip>
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import taskService from "@/services/task.service";
import type Task from "@/model/task/Task";
import DialogEditTask from "@/dialog/DialogEditTask.vue";
import TaskDateVChip from "@/components/TaskDateVChip.vue";

const route = useRoute();

const task = ref<Task>({} as Task);
const taskId = ref(Number(route.params.taskId));

const dialogEditTask = ref<typeof DialogEditTask>();
const taskDateVChip = ref<typeof TaskDateVChip>();

const emit = defineEmits(["taskUpdated"]);

const props = defineProps({
  courseRole: {
    type: String,
    required: true,
  },
});

onMounted(() => {
  loadTask();
});

const openSettings = () => {
  dialogEditTask.value?.openDialog(task.value).then(() => {
    loadTask();
    emit("taskUpdated");
  });
};

const loadTask = () => {
  taskService.getTask(taskId.value).then((response) => {
    task.value = response;
    taskDateVChip.value?.setDueDate(task.value.dueDate);
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

.align-items-center {
  display: flex;
  align-items: center;
}

.margin-right-5px {
  margin-right: 5px;
}
</style>
