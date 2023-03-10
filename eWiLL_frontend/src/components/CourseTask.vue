<template>
  <DialogShowFullDiagram ref="dialogShowFullDiagram" />

  <div class="task">
    <v-card>
      <v-card-title class="task-header-title">
        <h3 class="headline mb-0">{{ courseTask.title }}</h3>
        <v-spacer></v-spacer>
        <v-btn variant="text" icon="mdi-cog" color="dark-gray"></v-btn>
      </v-card-title>
      <v-card-text>
        <p>{{ courseTask.description }}</p>
        <br />
        <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label>
          {{ courseRole }}
        </v-chip>
      </v-card-text>
    </v-card>

    <div class="task-main">
      <div class="grid-left">
        <v-form>
          <v-select v-model="selectedCategoryId" label="Kategorie" variant="underlined" :items="categories" item-title="name" item-value="id" @update:model-value="updateDiagrams"></v-select>
          <v-select v-model="selectedDiagramId" label="Diagram" variant="underlined" :items="diagrams" item-title="name" item-value="id" @update:model-value="showSelectedDiagram"></v-select>
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
            <ModelingTool :key="modelingToolKey" class="modelPreview"></ModelingTool>
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
import { storeToRefs } from "pinia";

import ModelingTool from "@/components/ModelingTool.vue";
import DialogShowFullDiagram from "@/dialog/DialogShowFullDiagram.vue";

import diagramService from "@/services/diagram.service";

import CourseTask from "@/model/CourseTask";
import Category from "@/model/diagram/Category";
import Diagram from "@/model/diagram/Diagram";

import { useAuthUserStore } from "@/stores/authUserStore";
import { useDiagramStore } from "@/stores/diagramStore";

const diagramStore = useDiagramStore();

const modelingToolKey = storeToRefs(diagramStore).key;
const authUserStore = useAuthUserStore();

const courseTask: CourseTask = {
  id: 1,
  title: "Test Task",
  description: "This is a test task",
  course: {
    id: 1,
    title: "Test Course",
    description: "This is a test course",
    tasks: [],
  },
};

let courseRole: string = "Student";
const categories = ref<Category[]>([]);
const diagrams = ref<Diagram[]>([]);
const taskResults = ref<any[]>([
  { id: 1, name: "Item 1" },
  { id: 2, name: "Item 2" },
]);

const selectedCategoryId = ref<number>();
const selectedDiagramId = ref<number>();
const selectedResultTab = ref<any>();

const dialogShowFullDiagram = ref<typeof DialogShowFullDiagram>();

onMounted(() => {
  let userId = authUserStore.auth.user?.id;
  if (userId != undefined) {
    getCategories(userId);
  }

  diagramStore.createNewDiagram();
});

const openFullDiagram = () => {
  dialogShowFullDiagram.value?.openDialog();
};

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

const showSelectedDiagram = (diagramId: number) => {
  // console.log(diagrams.value.find((d) => d.id == diagramId));
  diagramStore.loadDiagram(diagrams.value.find((d) => d.id == diagramId) as Diagram);
};

const submitDiagram = () => {
  console.log("submit diagram");
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
</style>
