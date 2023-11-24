<template>
  <v-dialog v-model="editTaskDialog" width="70vw">
    <v-card :title="editTitle">
      <v-card-text>
        <v-form ref="taskForm" v-model="valid" class="task-form-grid">
          <div>
            <v-text-field v-model="currentTask.name" label="Name" :rules="nameRules" variant="underlined" color="primary"></v-text-field>
            <v-textarea v-model="currentTask.description" label="Beschreibung" :rules="descriptionRules" variant="underlined" color="primary"></v-textarea>
            <v-select v-model="selectedCategoryId" label="Ordner" :rules="modelRules" variant="underlined" :items="categories" item-title="name" item-value="id" color="primary" @update:model-value="updateDiagrams"></v-select>
            <v-select v-model="currentTask.solutionModelId" label="Musterdiagram" :rules="modelRules" variant="underlined" :items="diagrams" item-title="name" item-value="id" color="primary"></v-select>
          </div>
          <div>
            <v-text-field v-model="currentTask.dueDate" :rules="dueDateRules" label="Deadline" variant="underlined" color="primary" hint="DD.MM.YYYY HH:MM"></v-text-field>
            <v-select v-model="currentTask.eliability" :rules="liabilityRules" :items="liabilities" label="Verpflichtung" variant="underlined" color="primary" item-title="name" item-value="enum"></v-select>
            <v-select v-model="maxSubmissions" :items="arrayMaxSubmissions" label="Versuche" variant="underlined" color="primary" hint="0 = unbegrenzt" placeholder="0 = unbegrenzt" @update:model-value="updateMaxSubmissionsOnCurrentTask"></v-select>
            <div class="task-form-grid align-grid-elements">
              <v-select v-model="currentTask.taskLevel" :rules="levelrules" label="Level" variant="underlined" :items="levelOptions" color="primary" item-title="name" item-value="enum" @update:model-value="levelReckognition"></v-select>
              <v-btn :disabled="isDisabled" variant="plain"> Automatisch erkennen</v-btn>
            </div>
            <v-slider v-model="sliderPosition" :min="0" :max="2" :step="1" thumb-label label="Feedback Level" color="primary" hint="0 = Kein Feedback, 1 = Hinweis auf Fehler, 2 = Lösungsvorschläge" persistent-hint @update:model-value="updateShowLevel"></v-slider>
          </div>
        </v-form>
      </v-card-text>
      <v-card-actions class="card-actions">
        <v-btn v-if="!newTask" color="error" variant="flat" @click="deleteTask">Aufgabe löschen</v-btn>
        <v-spacer></v-spacer>

        <v-btn color="error" variant="flat" @click="_cancel"> Abbrechen </v-btn>
        <v-btn v-if="!loading" color="success" variant="flat" @click="_confirm"> Speichern </v-btn>
        <v-progress-circular v-if="loading" color="primary" indeterminate size="40"></v-progress-circular>
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
import { ref } from "vue";
import { useAuthUserStore } from "@/stores/authUserStore";
import { useRoute } from "vue-router";
import taskService from "@/services/task.service";
import router from "@/router";
import DialogConfirmVue from "../dialog/DialogConfirm.vue";
import FeedbackLevel from "@/enums/FeedbackLevel";
import TaskLevel from "@/enums/TaskLevel";

const arrayMaxSubmissions = Array.from(Array(100).keys());

const authUserStore = useAuthUserStore();
const route = useRoute();

const editTaskDialog = ref<boolean>(false);
const dialogConfirm = ref<typeof DialogConfirmVue>();

const snackbarFail = ref(false);
const editTitle = ref<string>("");
const newTask = ref(false);
const currentTask = ref<Task>({} as Task);
const loading = ref(false);
const maxSubmissions = ref();
const categories = ref<Category[]>([]);
const diagrams = ref<Diagram[]>([]);

// map enum to german names
const levelOptions = ref<any[]>([
  { name: "Leicht", enum: TaskLevel.EASY },
  { name: "Mittel", enum: TaskLevel.MODERATE },
  { name: "Schwer", enum: TaskLevel.HARD },
]);

const selectedLevel = ref("levelOptions");
const liabilities = ref<any[]>([
  { name: "Verpflichtend", enum: "MANDATORY" },
  { name: "Bonus", enum: "BONUS" },
  { name: "Optional", enum: "OPTIONAL" },
]);

const feedbackLevel = new Map<number, FeedbackLevel>([
  [0, FeedbackLevel.NOTHING],
  [1, FeedbackLevel.BASIC],
  [2, FeedbackLevel.INFO],
  [3, FeedbackLevel.DEBUG],
  [4, FeedbackLevel.ERROR],
]);
type ValidationRule = (value: string) => boolean | string;

const isDisabled = ref<boolean>(true);
const taskForm = ref<any>();
const valid = ref(false);
const selectedCategoryId = ref<number>();
const selectedDiagramId = ref<number>();
const sliderPosition = ref();
const nameRules = ref<ValidationRule[]>([(v: string) => !!v || "Name ist erforderlich"]);
const descriptionRules = ref<ValidationRule[]>([(v: string) => !!v || "Beschreibung ist erforderlich"]);
const modelRules = ref<ValidationRule[]>([(v: string) => !!v || "Musterdiagramm ist erforderlich"]);
const regex = /^([0-2][0-9]|3[0-1])\.(0[1-9]|1[0-2])\.\d{4} ([01][0-9]|2[0-3]):[0-5][0-9]$/;
const dueDateRules = ref<ValidationRule[]>([(v: string) => (!!v && regex.test(v)) || "Ungültiges Datum dd.mm.yyyy hh:mm"]);
const liabilityRules = ref<ValidationRule[]>([(v: string) => !!v || "Verpflichtung ist erforderlich"]);
const levelrules = ref<ValidationRule[]>([(v: string) => !!v || "Level ist erforderlich"]);

// empty, or should be a valid date and in the future
// const dueDateRules = ref<any>([(v: string) => !v || (new Date(v) > new Date() && !isNaN(new Date(v).getTime())) || "Ungültiges Datum"]);

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

// Necessary since the solution model is not owned by the user
const updateDiagramsIncludingSolutionModel = () => {
  diagramService.getDiagramById(currentTask.value.solutionModelId).then((response) => {
    selectedCategoryId.value = response.data.categoryId;
    let solutionModel = response.data;

    diagramService.getDiagramsByUserId(authUserStore.auth.user?.id as number).then((response) => {
      selectedDiagramId.value = undefined;
      diagrams.value = response.data.filter((d) => d.categoryId == selectedCategoryId.value);
      diagrams.value.push(solutionModel);
    });
  });
};

// #############################
// Promise
const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = (task?: Task) => {
  loading.value = false;
  diagrams.value = [];
  editTaskDialog.value = true;
  let userId = authUserStore.auth.user?.id!;
  getCategories(userId);

  if (task) {
    editTitle.value = "Aufgabe bearbeiten";
    currentTask.value = task;
    newTask.value = false;
    loadShowLevel(task.showLevel);
    updateDiagramsIncludingSolutionModel();
  } else {
    editTitle.value = "Neue Aufgabe erstellen";
    currentTask.value = {} as Task;
    currentTask.value.courseId = Number(route.params.id);
    currentTask.value.mediaType = "MODEL";
    currentTask.value.rulesetId = 0;
    currentTask.value.showLevel = FeedbackLevel.NOTHING;
    currentTask.value.taskLevel = TaskLevel.EASY;
    newTask.value = true;
  }

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _confirm = () => {
  loading.value = true;

  taskForm.value
    .validate()
    .then(() => {
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
              console.error(error);
              snackbarFail.value = true;
              loading.value = false;
            })
            .finally(() => {
              setTimeout(() => {
                loading.value = false;
              }, 2000);
            });
        } else {
          taskService
            .putTask(currentTask.value.id, currentTask.value)
            .then(() => {
              editTaskDialog.value = false;
              resolvePromise.value(true);
            })
            .catch((error) => {
              console.error(error);
              snackbarFail.value = true;
              loading.value = false; // Set loading to false here in case of error
            })
            .finally(() => {
              setTimeout(() => {
                loading.value = false;
              }, 2000);
            });
        }
      } else {
        loading.value = false;
      }
    })
    .catch(() => {
      loading.value = false;
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
  currentTask.value.showLevel = feedbackLevel.get(value)!;
};

const loadShowLevel = (level: string) => {
  feedbackLevel.forEach((value, key) => {
    if (value == level) sliderPosition.value = key;
  });
};
/*place holder for the level-Reckognition Function*/
const levelReckognition = (value: TaskLevel) => {
  switch (value) {
    case TaskLevel.EASY:
      selectedLevel.value = TaskLevel.EASY;
      break;
    case TaskLevel.MODERATE:
      selectedLevel.value = TaskLevel.MODERATE;
      break;
    case TaskLevel.HARD:
      selectedLevel.value = TaskLevel.HARD;
      break;
  }
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped>
.task-form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 10px;
}

.align-grid-elements {
  align-items: center;
}

.card-actions {
  padding: 1rem;
}

.automatic-btn {
  width: 100%;
  white-space: normal;
}

.level-col {
  margin-right: 50px;
}

.automatic-col {
  margin-top: 10px;
  height: 100px;
}
</style>
