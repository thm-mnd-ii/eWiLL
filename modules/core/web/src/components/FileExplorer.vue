<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <div>
    <!-- TODO: refactor DialogSaveDiagramVue like DialogConfirmVue to handle the dialog with promises -->
    <DialogSaveDiagramVue ref="dialogSave"></DialogSaveDiagramVue>
    <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>
    <DialogExportDiagramVue ref="dialogExport"></DialogExportDiagramVue>

    <v-card :elevation="0" class="explorer">
      <v-divider></v-divider>
      <v-card-actions class="btn-menue">
        <div>
          <v-btn icon="mdi-new-box" class="explorerBtn" :disabled="deleteActive" @click="deleteCurrentDiagram"></v-btn>
        </div>
        <div>
          <v-btn icon="mdi-content-save" class="explorerBtn" :disabled="deleteActive" @click="saveDialogButtonClick"></v-btn>
        </div>
        <div>
          <v-btn icon="mdi-export" class="explorerBtn" :disabled="deleteActive" @click="openExportDialog"></v-btn>
        </div>

        <div>
          <v-btn icon="mdi-undo" class="explorerBtn" :disabled="deleteActive || diagramStore.historyIndex <= 1" @click="diagramStore.undo"></v-btn>
        </div>
        <div>
          <v-btn icon="mdi-redo" class="explorerBtn" :disabled="deleteActive || diagramStore.historyIndex >= diagramStore.history.length" @click="diagramStore.redo"></v-btn>
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
      </v-card-actions>

      <v-card-text>
        <!-- TODO: Implement Search -->
        <!-- <v-text-field v-model="search" label="Search" class="search" @input="searchInput"></v-text-field> -->

        <v-list v-if="categoriesViewActive">
          <v-list-item v-for="[key] in map" :key="key.id" class="list-item" @click="categoryClicked(key)">
            <template #prepend>
              <v-icon v-if="deleteActive" size="20px" color="error">mdi-delete</v-icon>
              <v-icon v-if="!deleteActive" size="20px">mdi-folder</v-icon>
            </template>
            <v-list-item-title v-text="key.name"></v-list-item-title>
          </v-list-item>
        </v-list>

        <v-list v-if="!categoriesViewActive">
          <v-list-item v-for="diagram in displayDiagrams" :key="diagram.name" class="list-item" :value="diagram.name" @dblclick="loadDiagramIntoStore(diagram)" @click="diagramSingleClick(diagram)">
            <template #prepend>
              <v-icon v-if="deleteActive" size="20px" color="error">mdi-delete</v-icon>
              <v-icon v-if="!deleteActive" size="20px">mdi-file-document</v-icon>
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
import DialogExportDiagramVue from "../dialog/DialogExportDiagram.vue";
import diagramService from "../services/diagram.service";
import { useDiagramStore } from "../stores/diagramStore";
import { useAuthUserStore } from "../stores/authUserStore";
import { onMounted, ref } from "vue";
import { useToolManagementStore } from "@/stores/toolManagementStore";

const dialogConfirm = ref<typeof DialogConfirmVue>();
const dialogSave = ref<typeof DialogSaveDiagramVue>();
const dialogExport = ref<typeof DialogExportDiagramVue>();

const categoriesViewActive = ref(true);
const activeCategorie = ref<Category | null>(null);
const activeDiagramId = ref<number | null>(null);

const map = ref<Map<Category, Diagram[]>>(new Map());
const displayDiagrams = ref<Diagram[]>([]);

const deleteActive = ref(false);

const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();
const toolManagementStore = useToolManagementStore();

onMounted(() => {
  updateFiles()?.then(() => {
    selectActiveCourse();
  });

  if (diagramStore.diagram.id != undefined) {
    activeDiagramId.value = diagramStore.diagram.id;
  }
});

const updateFiles = () => {
  const uId = authUserStore.auth.user?.id as number;
  if (uId == undefined) {
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

const selectActiveCourse = () => {
  if (toolManagementStore.activeCourse != null) {
    let category: Category | undefined = Array.from(map.value.keys()).find((key) => key.name === toolManagementStore.activeCourse?.name);

    if (category != undefined) {
      selectCategory(category);
    }
  }
};

const moveToOverview = () => {
  categoriesViewActive.value = true;
  activeCategorie.value = null;
  updateFiles();
};

const categoryClicked = (category: Category) => {
  if (deleteActive.value) {
    if (dialogConfirm.value) {
      dialogConfirm.value.openDialog(`Lösche: ${category.name}`, "Willst du den Ordner wirklich löschen? Wenn du ihn löschst, werden auch alle Diagramme entfernt, die in diesem Ordner sind.").then((result: boolean) => {
        if (result) {
          diagramService.deleteCategory(category).then(() => {
            updateFiles();

            // If the active diagram is in the deleted category, create a new diagram and move to overview
            if (diagramStore.diagram.categoryId == category.id) {
              moveToOverview();
              diagramStore.createNewDiagram();
              activeDiagramId.value = null;
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
            updateFiles();

            // if selected diagram is deleted, create new diagram
            if (diagram.id == activeDiagramId.value) {
              diagramStore.createNewDiagram();
              activeDiagramId.value = null;
            }
          });
        }
      });
    }
  }
};

const saveDialogButtonClick = () => {
  activeDiagramId.value = diagramStore.diagram.id;
  dialogSave.value?.openDialog(activeDiagramId.value).then((result: boolean) => {
    if (result) {
      updateFiles();
      activeDiagramId.value = diagramStore.diagram.id;
    }
  });
};

const deleteCurrentDiagram = () => {
  dialogConfirm.value?.openDialog("Diagramm zurücksetzen", 'Willst du deinen bisherigen Fortschritt löschen? Wenn du auf "Zurücksetzen" klickst, wird das gesamte Diagramm zurückgesetzt und du kannst von Grund auf neu anfangen.', "Zurücksetzen").then((result: boolean) => {
    if (result) {
      diagramStore.createNewDiagram();
      localStorage.removeItem("diagram");
    }
  });
};

const openExportDialog = () => {
  dialogExport.value?.openDialog();
};

defineExpose({
  updateFiles,
});
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
  font-size: 0.85rem;
  line-height: 0.95rem;
}

.list-item {
  padding: 0px;
  padding-inline-start: 0px !important;
  max-height: 30px !important;
  height: 20px !important;
  min-height: 0px !important;
}

.deleteBtnActive {
  background-color: rgb(var(--v-theme-error));
}

#saveAsNewBtn {
  padding: 2px;
  margin-left: 10px;
}
</style>
