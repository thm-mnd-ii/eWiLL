<template>
  <v-dialog v-model="editTaskDialog" width="70vw">
    <v-card :title="editTitle">
      <v-card-text>
        <v-form ref="taskForm" v-model="valid" class="taskForm">
          <div>
            <v-text-field v-model="currentTask.name" label="Name" :rules="nameRules" variant="underlined" color="#81ba24"></v-text-field>
            <v-textarea v-model="currentTask.description" label="Beschreibung" :rules="descriptionRules" variant="underlined" color="#81ba24"></v-textarea>
            <v-select v-model="selectedCategoryId" required label="Kategorie" variant="underlined" :items="categories" item-title="name" item-value="id" color="#81ba24" @update:model-value="updateDiagrams"></v-select>
            <v-select v-model="currentTask.solutionModelId" label="Musterdiagram" variant="underlined" :items="diagrams" item-title="name" item-value="id" color="#81ba24"></v-select>
          </div>
          <div>
            <v-text-field v-model="currentTask.dueDate" :rules="dueDateRules" label="Deadline" variant="underlined" color="#81ba24" hint="DD.MM.YYYY HH:MM"></v-text-field>
            <v-select v-if="!workInProgress" v-model="currentTask.mediaType" :items="['Model', 'Text']" label="Medientyp" variant="underlined" color="#81ba24"></v-select>
            <v-select v-if="!workInProgress" v-model="currentTask.rulesetId" :items="rulesets" item-title="name" label="Regelsatz" variant="underlined" color="#81ba24" item-value="id"></v-select>
            <v-select v-model="currentTask.eliability" :items="liabilities" label="Verpflichtung" variant="underlined" color="#81ba24" item-title="name" item-value="enum"></v-select>
            <v-select v-model="maxSubmissions" :items="arrayMaxSubmissions" label="Versuche" variant="underlined" color="#81ba24" hint="0 = unbegrenzt" placeholder="0 = unbegrenzt" @update:model-value="updateMaxSubmissionsOnCurrentTask"></v-select>
            <v-slider v-model="sliderPosition" :min="0" :max="2" :step="1" thumb-label label="Feedback Level" color="#81ba24" hint="0 = Kein Feedback, 1 = Hinweis auf Fehler, 2 = Lösungsvorschläge" @update:model-value="updateShowLevel"></v-slider>
          </div>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn v-if="!newTask" class="btn-red" @click="deleteTask">Aufgabe löschen</v-btn>
        <v-spacer></v-spacer>
        <v-btn class="btn-red" @click="_cancel"> Abbrechen </v-btn>
        <v-btn id="btn-confirm" type="submit" @click="_confirm"> Speichern </v-btn>
      </v-card-actions>
    </v-card>
    <v-snackbar v-model="snackbarFail" :timeout="3000"> Ein Fehler ist aufgetreten, bitte versuchen Sie es erneut </v-snackbar>
  </v-dialog>
  <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>
</template>

<script setup lang="ts">
import Task from "@/model/task/Task";
import Category from "@/model/diagram/Category";
import Diagram from "@/model/diagram/Diagram";
import diagramService from "@/services/diagram.service";
import { ref, onMounted } from "vue";
import { useAuthUserStore } from "@/stores/authUserStore";
import { useRoute } from "vue-router";
import taskService from "@/services/task.service";
import router from "@/router";
import DialogConfirmVue from "../dialog/DialogConfirm.vue";

const arrayMaxSubmissions = Array.from(Array(100).keys());

const workInProgress = ref(true);

const authUserStore = useAuthUserStore();
const route = useRoute();

const editTaskDialog = ref<boolean>(false);
const dialogConfirm = ref<typeof DialogConfirmVue>();

const snackbarFail = ref(false);
const editTitle = ref<string>("");
const newTask = ref(false);
const currentTask = ref<Task>({} as Task);

const maxSubmissions = ref();
const categories = ref<Category[]>([]);
const diagrams = ref<Diagram[]>([]);
const rulesets = ref<any[]>([
  { id: 1, name: "Regelsatz 1" },
  { id: 2, name: "Regelsatz 2" },
]);
const liabilities = ref<any[]>([
  { name: "Verpflichtend", enum: "MANDATORY" },
  { name: "Bonus", enum: "BONUS" },
  { name: "Optional", enum: "OPTIONAL" },
]);
const resultLevels = new Map<number, string>([
  [0, "NOTHING"],
  [1, "BASIC"],
  [2, "INFO"],
  [3, "DEBUG"],
  [4, "ERROR"],
]);

const taskForm = ref<any>();
const valid = ref(false);
const selectedCategoryId = ref<number>();
const selectedDiagramId = ref<number>();
const sliderPosition = ref();
const nameRules = ref<any>([(v: string) => !!v || "Name ist erforderlich"]);
const descriptionRules = ref<any>([(v: string) => !!v || "Beschreibung ist erforderlich"]);
const regex = /^([0-2][0-9]|3[0-1])\.(0[1-9]|1[0-2])\.\d{4} ([01][0-9]|2[0-3]):[0-5][0-9]$/;
const dueDateRules = ref<any>([(v: string) => !v || regex.test(v) || "Ungültiges Datum dd.mm.yyyy hh:mm"]);

// empty, or should be a valid date and in the future
// const dueDateRules = ref<any>([(v: string) => !v || (new Date(v) > new Date() && !isNaN(new Date(v).getTime())) || "Ungültiges Datum"]);

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

const setModelAndCategory = () => {
  diagramService.getDiagramById(currentTask.value.solutionModelId).then((response) => {
    selectedCategoryId.value = response.data.categoryId;
    updateDiagrams(selectedCategoryId.value!);
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
    newTask.value = false;
    setModelAndCategory();
    loadShowLevel(task.showLevel);
  } else {
    editTitle.value = "Neue Aufgabe erstellen";
    currentTask.value = {} as Task;
    currentTask.value.courseId = Number(route.params.id);
    currentTask.value.mediaType = "MODEL";
    currentTask.value.rulesetId = 0;
    currentTask.value.showLevel = "NOTHING";
    newTask.value = true;
  }

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _confirm = () => {
  taskForm.value.validate().then(() => {
    if (valid.value) {
      currentTask.value.mediaType = currentTask.value.mediaType.toUpperCase();
      if (newTask.value) {
        taskService
          .postTask(currentTask.value)
          .then(() => {
            editTaskDialog.value = false;
            resolvePromise.value(true);
          })
          .catch((error) => {
            console.log(error);
          });
      } else {
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
    }
  });
};

const _cancel = () => {
  categories.value = [];
  selectedCategoryId.value = undefined;
  editTaskDialog.value = false;
  resolvePromise.value(false);
};

const deleteTask = () => {
  dialogConfirm.value?.openDialog(`Lösche Aufgabe`, "Willst du die Aufgabe wirklich löschen?").then((result: boolean) => {
    if (result) {
      taskService.deleteTask(currentTask.value.id).then(() => {
        router.push("/course/" + currentTask.value.courseId);
      });
    }
  });
};

const updateMaxSubmissionsOnCurrentTask = (submissions: any) => {
  if (submissions == 0) currentTask.value.maxSubmissions = 999;
  else currentTask.value.maxSubmissions = submissions;
};

const updateShowLevel = (value: any) => {
  currentTask.value.showLevel = resultLevels.get(value)!;
};

const loadShowLevel = (level: string) => {
  resultLevels.forEach((value, key) => {
    if (value == level) sliderPosition.value = key;
  });
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

#btn-confirm {
  background-color: #81ba24;
  color: #ffffff;
}

.btn-red {
  background-color: #db3e1f;
  color: #ffffff;
}
</style>
