<template>
  <v-dialog v-model="editTaskDialog" width="70vw">
    <v-card :title="editTitle">
      <v-card-text>
        <v-form ref="taskForm" v-model="valid" class="taskForm">
          <div>
            <v-text-field v-model="currentTask.name" label="Name" :rules="nameRules" variant="underlined" required></v-text-field>
            <v-textarea v-model="currentTask.description" label="Beschreibung" :rules="descriptionRules" variant="underlined" required></v-textarea>
            <v-select v-model="selectedCategoryId" label="Kategorie" variant="underlined" :items="categories" item-title="name" item-value="id" @update:model-value="updateDiagrams"></v-select>
            <v-select v-model="selectedDiagramId" label="Musterdiagram" variant="underlined" :items="diagrams" item-title="name" item-value="id"></v-select>
          </div>
          <div>
            <v-text-field v-model="currentTask.dueDate" label="Deadline" :rules="dueDateRules" variant="underlined"></v-text-field>
            <v-select v-model="currentTask.mediaType" :items="['Modeling', 'Text']" label="Medientyp" variant="underlined" required></v-select>
            <v-select v-model="currentTask.rulesetId" :items="rulesets" item-title="name" label="Regelsatz" variant="underlined" required></v-select>
          </div>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="_cancel"> Abbrechen </v-btn>
        <v-btn variant="text" @click="_confirm"> Speichern </v-btn>
      </v-card-actions>
    </v-card>
    <v-snackbar v-model="snackbarFail" :timeout="3000"> Ein Fehler ist aufgetreten, bitte versuchen Sie es erneut </v-snackbar>
  </v-dialog>
</template>

<script setup lang="ts">
import Task from "@/model/task/Task";
import Category from "@/model/diagram/Category";
import Diagram from "@/model/diagram/Diagram";
import diagramService from "@/services/diagram.service";
import { ref, onMounted } from "vue";

import { useAuthUserStore } from "@/stores/authUserStore";
import taskService from "@/services/task.service";

const authUserStore = useAuthUserStore();

const editTaskDialog = ref<boolean>(false);

const snackbarFail = ref(false);
const editTitle = ref<string>("");
const currentTask = ref<Task>({} as Task);

const categories = ref<Category[]>([]);
const diagrams = ref<Diagram[]>([]);
const rulesets = ref<any[]>([
  { id: 1, name: "Regelsatz 1" },
  { id: 2, name: "Regelsatz 2" },
]);

const taskForm = ref<any>();
const valid = ref(false);
const selectedCategoryId = ref<number>();
const selectedDiagramId = ref<number>();
const nameRules = ref<any>([(v: string) => !!v || "Name ist erforderlich"]);
const descriptionRules = ref<any>([(v: string) => !!v || "Beschreibung ist erforderlich"]);

// empty, or should be a valid date and in the future
const dueDateRules = ref<any>([(v: string) => !v || (new Date(v) > new Date() && !isNaN(new Date(v).getTime())) || "Ungültiges Datum"]);

onMounted(() => {
  let userId = authUserStore.auth.user?.id!;
  getCategories(userId);
});

const getCategories = (uId: number) => {
  diagramService.getCategories(uId).then((response) => {
    categories.value = response.data;
  });
};

const updateDiagrams = (categoryId: number) => {
  diagramService.getDiagramsByUserId(authUserStore.auth.user?.id as number).then((response) => {
    selectedDiagramId.value = undefined;
    diagrams.value = response.data.filter((d) => d.categoryId == categoryId);
  });
};

// #############################
// Promis
const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = (task?: Task) => {
  editTaskDialog.value = true;

  if (task) {
    editTitle.value = "Aufgabe bearbeiten";
    currentTask.value = task;
  } else {
    editTitle.value = "Neue Aufgabe erstellen";
    currentTask.value = {} as Task;
  }

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _confirm = () => {
  taskForm.value.validate().then(() => {
    if (valid.value) {
      taskService
        .putTask(currentTask.value.id, currentTask.value)
        .then(() => {
          editTaskDialog.value = false;
          resolvePromise.value(true);
        })
        .catch(() => {
          snackbarFail.value = true;
        });
    }
  });
};

const _cancel = () => {
  editTaskDialog.value = false;
  resolvePromise.value(false);
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped>
.taskForm {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 10px;
}
</style>
