<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <div>
    <!-- TODO: refactor DialogSaveDiagramVue like DialogConfirmVue to handle the dialog with promises -->
    <DialogSaveDiagramVue ref="dialogSave"></DialogSaveDiagramVue>
    <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>

    <v-card :elevation="0" class="explorer">
      <v-divider></v-divider>
      <v-card-actions class="btn-menue">
        <div>
          <v-btn icon="mdi-new-box" class="explorerBtn" :disabled="deleteActive" @click="createNewDiagram"></v-btn>
        </div>
        <div>
          <v-btn icon="mdi-content-save" class="explorerBtn" :disabled="deleteActive" @click="saveDialogButtonClick"></v-btn>
        </div>
        <div>
          <v-btn icon="mdi-delete" class="explorerBtn" :class="{ deleteBtnActive: deleteActive }" @click="deleteActive = !deleteActive"></v-btn>
        </div>

        <div>
          <v-btn icon="mdi-arrow-left" class="explorerBtn" :disabled="deleteActive || categoriesViewActive" @click="moveToOverview"></v-btn>
        </div>
        <!-- reload -->
        <div>
          <v-btn icon="mdi-refresh" class="explorerBtn" :disabled="deleteActive" @click="updateFiles"></v-btn>
        </div>
        <div></div>
        <div>
          <v-btn icon="mdi-undo" class="explorerBtn" :disabled="deleteActive || diagramStore.historyIndex <= 1" @click="diagramStore.undo"></v-btn>
        </div>
        <div>
          <v-btn icon="mdi-redo" class="explorerBtn" :disabled="deleteActive || diagramStore.historyIndex >= diagramStore.history.length" @click="diagramStore.redo"></v-btn>
        </div>
      </v-card-actions>
      <!-- Breaking line -->
      <v-divider></v-divider>
      <v-card-text>
        <!-- TODO: Implement Search -->
        <!-- <v-text-field v-model="search" label="Search" class="search" @input="searchInput"></v-text-field> -->

        <v-list v-if="categoriesViewActive">
          <v-list-item v-for="[key] in map" :key="key.id" class="list-item" @click="categoryClicked(key)">
            <template #prepend>
              <v-icon v-if="deleteActive" size="15px" color="error">mdi-delete</v-icon>
              <v-icon v-if="!deleteActive" size="15px">mdi-folder</v-icon>
            </template>
            <v-list-item-title v-text="key.name"></v-list-item-title>
          </v-list-item>
        </v-list>

        <v-list v-if="!categoriesViewActive">
          <v-list-item v-for="diagram in displayDiagrams" :key="diagram.name" class="list-item" :value="diagram.name" @dblclick="loadDiagramIntoStore(diagram)" @click="diagramSingleClick(diagram)">
            <template #prepend>
              <v-icon v-if="deleteActive" size="15px" color="error">mdi-delete</v-icon>
              <v-icon v-if="!deleteActive" size="15px">mdi-file-document</v-icon>
            </template>
            <v-list-item-title v-text="diagram.name"></v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card-text>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import Diagram from "../model/diagram/Diagram";
import Category from "../model/diagram/Category";
import DialogSaveDiagramVue from "../dialog/DialogSaveDiagram.vue";
import DialogConfirmVue from "../dialog/DialogConfirm.vue";
import diagramService from "../services/diagram.service";
import { useDiagramStore } from "../stores/diagramStore";
import { useAuthUserStore } from "../stores/authUserStore";
import { onMounted, ref } from "vue";

const dialogConfirm = ref<typeof DialogConfirmVue>();
const dialogSave = ref<typeof DialogSaveDiagramVue>();

const categoriesViewActive = ref(true);
const activeCategorie = ref<Category | null>(null);
const activeDiagramId = ref<number | null>(null);

const categoryNames = ref<string[]>([]);
const map = ref<Map<Category, Diagram[]>>(new Map());
const displayDiagrams = ref<Diagram[]>([]);

const deleteActive = ref(false);

const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();

onMounted(() => {
  updateFiles();
});

const updateFiles = () => {
  const uId = authUserStore.auth.user?.id as number;
  if (uId == undefined) {
    console.log("userId is undefined");
    return;
  }

  return new Promise<void>((resolve, reject) => {
    diagramService
      .getCategories(uId)
      .then((categories) => {
        diagramService
          .getDiagramsByUserId(uId)
          .then((userDiagrams) => {
            map.value = diagramService.getDiagramsWithCategory(categories.data, userDiagrams.data);
            if (activeCategorie.value != null) {
              displayDiagrams.value = [] as Diagram[];
              map.value.forEach((value, key) => {
                if (key.id == activeCategorie.value?.id) {
                  displayDiagrams.value = value;
                }
              });
            }
            resolve();
          })
          .catch((error) => {
            console.log(error);
            reject();
          });
      })
      .catch((error) => {
        console.log(error);
        reject();
      });
  });
};

const moveToOverview = () => {
  categoriesViewActive.value = true;
  activeCategorie.value = null;
  updateFiles();
};

const categoryClicked = (category: Category) => {
  if (deleteActive.value) {
    if (dialogConfirm.value) {
      dialogConfirm.value.openDialog(`Lösche: ${category.name}`, "Willst du die Kategorie wirklich löschen? Wenn du sie löscht, werden auch alle Diagramme gelöscht, die in dieser Kategorie sind.").then((result: boolean) => {
        if (result) {
          diagramService.deleteCategory(category).then(() => {
            updateFiles();

            // If the active diagram is in the deleted category, create a new diagram and move to overview
            if (diagramStore.diagram.categoryId == category.id) {
              moveToOverview();
              diagramStore.createNewDiagram();
            }
          });
        }
      });
    }
  } else {
    selectCategory(category);
  }
};

const selectCategory = (category: Category) => {
  categoriesViewActive.value = false;
  activeCategorie.value = category;
  displayDiagrams.value = [];
  map.value.get(category)?.forEach((diagram) => {
    displayDiagrams.value.push(diagram);
  });
};

const loadDiagramIntoStore = (diagram: Diagram) => {
  if (!deleteActive.value) {
    activeDiagramId.value = diagram.id;
    diagramStore.loadDiagram(diagram);
  }
};

const diagramSingleClick = (diagram: Diagram) => {
  if (deleteActive.value) {
    if (dialogConfirm.value) {
      dialogConfirm.value.openDialog(`Lösche: ${diagram.name}`, "Willst du das Diagramm wirklich löschen?").then((result: boolean) => {
        if (result) {
          diagramService.deleteDiagram(diagram).then(() => {
            console.log("Diagram deleted");
            console.log(activeCategorie.value);
            updateFiles();

            // if selected diagram is deleted, create new diagram
            if (diagram.id == activeDiagramId.value) {
              diagramStore.createNewDiagram();
            }
          });
        }
      });
    }
  }
};

const saveDialogButtonClick = () => {
  dialogSave.value?.openDialog(activeDiagramId.value).then((result: boolean) => {
    if (result) {
      updateFiles();
    }
  });
  categoryNames.value.length = 0;
};

const createNewDiagram = () => {
  dialogConfirm.value?.openDialog("Neues Diagramm", 'Willst du ein neues Diagramm erstellen? Wenn du auf "Erstellen" klickst wird möglicherweise der aktuelle Stand deines Diagrams gelöscht.', "Erstellen").then((result: boolean) => {
    if (result) {
      diagramStore.createNewDiagram();
    }
  });
};
</script>

<style scoped lang="scss">
.explorer {
  z-index: 1;
  position: relative;
  width: 100%;
}

// grid
.btn-menue {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  grid-template-areas: "btn1 btn2 btn3" "btn4 btn5 btn6";
  grid-gap: 15px;
  padding: 5px;
  margin-top: 5px;

  div {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 30px;
    border-radius: 3px;
    cursor: pointer;
    transition: all 0.2s ease-in-out;

    .explorerBtn {
      height: 30px;
      border-radius: 3px;

      &:hover {
        margin-bottom: 5px;
      }
    }
  }
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

.deleteBtnActive {
  background-color: red;
}

#saveAsNewBtn {
  padding: 2px;
  margin-left: 10px;
}
</style>