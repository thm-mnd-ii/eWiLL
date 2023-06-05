<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div class="task">
    <v-card>
      <v-card-title class="task-header-title">
        <h3 class="headline mb-0">{{ task.name }}</h3>
        <v-spacer></v-spacer>
        <v-btn v-if="courseRole != 'STUDENT'" variant="text" icon="mdi-cog" color="dark-gray"></v-btn>
      </v-card-title>
      <v-card-text>
        <p>{{ task.description }}</p>
        <br />
        <div class="align-items-center">
          <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label>
            {{ courseRole }}
          </v-chip>
          <v-spacer></v-spacer>
          <v-chip v-if="task.maxSubmissions != 999" class="margin-right-5px">Versuche: {{ task.maxSubmissions }}</v-chip>
          <v-chip v-if="task.maxSubmissions == 999" class="margin-right-5px">Versuche: unbegrenzt</v-chip>
          <v-chip v-if="task.eliability == 'BONUS'" color="green">Bonus</v-chip>
          <v-chip v-if="task.eliability == 'MANDATORY'" color="red">Verpflichtend</v-chip>
          <v-chip v-if="task.eliability == 'OPTIONAL'" color="yellow">Optional</v-chip>
        </div>
      </v-card-text>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import Task from "../model/task/Task";

const task = ref<Task>({} as Task);
const courseRole = ref();
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
