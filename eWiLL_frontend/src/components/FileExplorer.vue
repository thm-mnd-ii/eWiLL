<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <DialogSaveDiagramVue :open-dialog="saveDialog" :selected-diagram-id="activeDiagramId" @close-dialog="saveDialog = false"></DialogSaveDiagramVue>

  <DialogDeleteDiagramVue :open-dialog="deleteDialog"></DialogDeleteDiagramVue>

  <div class="explorer">
    <v-btn icon="mdi-home" class="explorerBtn" :disabled="deleteActive" @click="homeButtonClick"></v-btn>
    <v-btn icon="mdi-content-save" class="explorerBtn" :disabled="deleteActive" @click="saveDialogButtonClick"></v-btn>
    <v-btn icon="mdi-delete" class="explorerBtn" :class="{ deleteBtnActive: deleteActive }" @click="deleteActive = !deleteActive"></v-btn>

    <v-list v-if="categoriesViewActive">
      <v-list-item v-for="[key] in map" :key="key" class="list-item" @click="categoryClicked(key)">
        <template #prepend>
          <v-icon size="15px">mdi-folder</v-icon>
        </template>
        <v-list-item-title v-text="key"></v-list-item-title>
      </v-list-item>
    </v-list>

    <v-list v-if="!categoriesViewActive">
      <v-list-item v-for="diagram in displayDiagrams" :key="diagram.name" class="list-item" :value="diagram.name" @dblclick="diagramDoubleClick(diagram)" @click="diagramSingleClick(diagram)">
        <template #prepend>
          <v-icon size="15px">mdi-file-document</v-icon>
        </template>
        <v-list-item-title v-text="diagram.name"></v-list-item-title>
      </v-list-item>
    </v-list>
  </div>
</template>

<script setup lang="ts">
import Diagram from "../model/diagram/Diagram";
import Category from "../model/diagram/Category";
import DialogSaveDiagramVue from "../dialog/DialogSaveDiagram.vue";
import DialogDeleteDiagramVue from "../dialog/DialogDeleteDiagram.vue";
import diagramService from "../services/diagram.service";
import { useDiagramStore } from "../stores/diagramStore";
import { useAuthUserStore } from "../stores/authUserStore";
import { onMounted, ref } from "vue";

const deleteDialog = ref<boolean>(false);

const categoriesViewActive = ref(true);
const activeCategorie = ref("");
const activeDiagramId = ref<number | null>(null);

const categoryNames = ref<string[]>([]);
const map = ref<Map<string, Diagram[]>>(new Map<string, Diagram[]>());
const displayDiagrams = ref<Diagram[]>([]);
//Constants for saving dialog
const saveDialog = ref(false);

const deleteActive = ref(false);
const deleteProp = ref("");
const deleteItemCategory = ref();
const deleteItemDiagram = ref();

const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();

onMounted(() => {
  //displayDiagrams.value.length = 0;

  let userId = authUserStore.auth.user?.id;
  if (userId != undefined) {
    updateFiles(userId);
  } else {
    console.log("userId is undefined");
  }
});

const updateFiles = (uId: number) => {
  diagramService
    .getCategories(uId)
    .then((response) => {
      let categories = response.data as Category[];
      diagramService
        .getDiagramsByUserId(uId)
        .then((response) => {
          let userDiagrams = response.data as Diagram[];

          map.value = diagramService.getDiagramsWithCategory(categories, userDiagrams);
          displayDiagrams.value = [];
          map.value.get(activeCategorie.value)?.forEach((diagram) => {
            displayDiagrams.value.push(diagram);
          });
        })
        .catch((error) => console.log(error));
    })
    .catch((error) => {
      console.log(error);
    });
};

const homeButtonClick = () => {
  categoriesViewActive.value = true;
};

const categoryClicked = (category: string) => {
  console.log(category);

  if (deleteActive.value) {
    deleteProp.value = category;
    deleteItemDiagram.value = null;
    deleteItemCategory.value = category;
  } else {
    categoriesViewActive.value = false;
    activeCategorie.value = category;
    displayDiagrams.value.length = 0;
    map.value.get(category)?.forEach((diagram) => {
      displayDiagrams.value.push(diagram);
    });
  }
};

const diagramDoubleClick = (diagram: Diagram) => {
  console.log(diagram);
  activeDiagramId.value = diagram.id;

  diagramStore.reset();
  diagramStore.diagram = diagram;
};

const diagramSingleClick = (diagram: Diagram) => {
  if (deleteActive.value) {
    deleteProp.value = diagram.name;
    deleteItemDiagram.value = diagram;
    deleteItemCategory.value = null;
  }
};

const saveDialogButtonClick = () => {
  saveDialog.value = true;
  categoryNames.value.length = 0;
};
</script>

<style scoped>
.explorer {
  background-color: rgb(255, 255, 255);
  z-index: 1;
  position: absolute;
  top: 50px;
  left: 5px;
  padding: 10px;
  width: 200px;
  height: 300px;
  border-radius: 5px;
  box-shadow: 4px 4px 10px 0px rgba(15, 33, 47, 0.421);
}

.v-list {
  margin: 0;
  padding: 0;
}

.v-list-item-title {
  font-size: 0.7rem;
  line-height: 0.7rem;
}

.list-item {
  padding: 0px;
  padding-inline-start: 0px !important;
  max-height: 30px !important;
  height: 20px !important;
  min-height: 0px !important;
}

.explorerBtn {
  margin: 3px;
  border-radius: 3px !important;
  height: 35px;
}

.deleteBtnActive {
  background-color: red;
}

#saveAsNewBtn {
  padding: 2px;
  margin-left: 10px;
}
</style>
