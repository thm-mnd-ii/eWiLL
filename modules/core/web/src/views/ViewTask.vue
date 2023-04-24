<template>
  <DialogShowFullDiagram ref="dialogShowFullDiagram" />
  <DialogEditTask ref="dialogEditTask" />

  <div class="task">
    <v-card>
      <v-card-title class="task-header-title">
        <h3 class="headline mb-0">{{ task.name }}</h3>
        <v-spacer></v-spacer>
        <v-btn v-if="courseRole != 'STUDENT'" variant="text" icon="mdi-cog" color="dark-gray" @click="openSettings"></v-btn>
      </v-card-title>
      <v-card-text>
        <p>{{ task.description }}</p>
        <br />
        <div class="align-items-center">
          <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label>
            {{ courseRole }}
          </v-chip>
          <v-spacer></v-spacer>
          <v-chip v-if="task.eliability == 'BONUS'" color="green">Bonus</v-chip>
          <v-chip v-if="task.eliability == 'MANDATORY'" color="red">Verpflichtend</v-chip>
          <v-chip v-if="task.eliability == 'OPTIONAL'" color="yellow">Optional</v-chip>
        </div>
      </v-card-text>
    </v-card>

    <div class="task-main">
      <div class="grid-left">
        <v-form>
          <v-select v-model="selectedCategoryId" label="Kategorie" variant="underlined" :items="categories" item-title="name" :disabled="courseRole != 'STUDENT'" item-value="id" @update:model-value="updateDiagrams"></v-select>
          <v-select v-model="selectedDiagramId" label="Diagram" variant="underlined" :items="diagrams" item-title="name" item-value="id" :disabled="courseRole != 'STUDENT'" @update:model-value="showSelectedDiagram"></v-select>
        </v-form>

        <v-card class="preview-container">
          <v-card-title class="task-header-title">
            <h3 class="headline mb-0">Preview</h3>
            <v-spacer></v-spacer>
            <v-btn icon variant="text" color="dark-gray" @click="openFullDiagram">
              <v-icon icon="mdi-fullscreen" size="x-large"></v-icon>
            </v-btn>
          </v-card-title>
          <v-card-text class="modeling-container">
            <ModelingTool :key="modelingToolKey" class="modelPreview" :is-editable="false"></ModelingTool>
          </v-card-text>
        </v-card>
      </div>
      <div class="grid-right">
        <v-btn class="submit-btn" color="dark-gray" variant="flat" @click="submitDiagram">prüfen</v-btn>
        <br />
        <div class="task-trials-caption font-weight-medium">
          <span>Auswertungsergebnisse</span>
          <span>Versuch 5 / 10</span>
        </div>
        <v-card class="task-trials-tabs">
          <v-tabs v-model="selectedResultTab" bg-color="teal-darken-3" slider-color="teal-lighten-3">
            <v-tab v-for="tab in taskResults" :key="tab.id" :value="tab.id">
              {{ "Ergebnis " + tab.id }}
            </v-tab>
          </v-tabs>
          <v-window v-model="selectedResultTab">
            <v-window-item v-for="tab in taskResults" :key="tab.id" :value="tab.id">
              <v-card flat>
                <v-card-text class="task-trials-text">
                  <p>Tab {{ tab.id }}</p>
                </v-card-text>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn class="" append-icon="mdi-open-in-new" color="dark-gray" variant="text"> Zeige Fehler im Diagram </v-btn>
                </v-card-actions>
              </v-card>
            </v-window-item>
          </v-window>
        </v-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import courseService from "../services/course.service";
import taskService from "../services/task.service";
import categoryService from "../services/category.service";
import Task from "../model/task/Task";
import DialogEditTask from "@/dialog/DialogEditTask.vue";

import Category from "@/model/diagram/Category";
import Diagram from "@/model/diagram/Diagram";
import DialogShowFullDiagram from "@/dialog/DialogShowFullDiagram.vue";
import diagramService from "@/services/diagram.service";
import { useDiagramStore } from "@/stores/diagramStore";
import { storeToRefs } from "pinia";
import ModelingTool from "@/components/ModelingTool.vue";

const route = useRoute();
const router = useRouter();
const authUserStore = useAuthUserStore();
const diagramStore = useDiagramStore();
const modelingToolKey = storeToRefs(diagramStore).key;

const task = ref<Task>({} as Task);
const taskId = ref(Number(route.params.taskId));
const courseId = ref(Number(route.params.courseId));
const courseRole = ref("");
const userId = ref(authUserStore.auth.user?.id!);

const dialogEditTask = ref<typeof DialogEditTask>();
const dialogShowFullDiagram = ref<typeof DialogShowFullDiagram>();

const categories = ref<Category[]>([]);
const selectedCategoryId = ref<number>();
const diagrams = ref<Diagram[]>([]);
const selectedDiagramId = ref<number>();

const selectedResultTab = ref<any>();
const taskResults = ref<any[]>([
  { id: 1, name: "Item 1" },
  { id: 2, name: "Item 2" },
]);

onMounted(() => {
  courseService.getUserRoleInCourse(userId.value!, courseId.value).then((response) => {
    if (response == "NONE") {
      router.push("/course/" + route.params.courseId + "/signup");
    } else {
      courseRole.value = response;
      loadTask();
      loadCategories();
      diagramStore.createNewDiagram();
    }
  });
});

const openSettings = () => {
  dialogEditTask.value?.openDialog(task.value).then((result: boolean) => {
    if (result) {
      // TODO: reload task
    }
  });
};

const loadTask = () => {
  taskService.getTask(taskId.value).then((response) => {
    task.value = response;
    if (courseRole.value != "STUDENT") loadSolutionModel();
  });
};

const openFullDiagram = () => {
  dialogShowFullDiagram.value?.openDialog("");
};

const updateDiagrams = (categoryId: number) => {
  diagramService.getDiagramsByUserId(authUserStore.auth.user?.id as number).then((response) => {
    selectedDiagramId.value = undefined;
    diagrams.value = response.data.filter((d) => d.categoryId == categoryId);
  });
};

const showSelectedDiagram = (diagramId: number) => {
  // console.log(diagrams.value.find((d) => d.id == diagramId));
  diagramStore.loadDiagram(diagrams.value.find((d) => d.id == diagramId) as Diagram);
};

const submitDiagram = () => {
  console.log("submit diagram");
};

const loadCategories = () => {
  categoryService.getByUserId(userId.value).then((data) => {
    categories.value = data;
  });
};

const loadSolutionModel = () => {
  diagramService.getDiagramById(task.value.solutionModelId).then((response) => {
    const categoryId = response.data.categoryId;
    selectedCategoryId.value = categoryId;
    diagrams.value.push(response.data);
    selectedDiagramId.value = task.value.solutionModelId;
    showSelectedDiagram(selectedDiagramId.value);
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

.task-main {
  /* grid with left and right side */
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  grid-gap: 40px;
  margin: 20px 10px;
}

.submit-btn {
  /* center in grid */
  justify-self: center;
}

.grid-right {
  /* center */
  display: flex;
  flex-direction: column;
  align-items: center;

  width: 100%;
}

.task-trials-caption {
  /* 1 item left 1 item right */
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.task-trials-tabs {
  margin: 20px 0;
  width: 100%;
}

.task-trials-text {
  min-height: 100px;
}

.modeling-container {
  width: 100%;
  height: 350px;
}

.modelPreview {
  position: relative;
  width: 100%;
  height: 100%;
}

.align-items-center {
  display: flex;
  align-items: center;
}
</style>
