<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <div>
    <!-- TODO: refactor DialogSaveDiagramVue like DialogConfirmVue to handle the dialog with promises -->
    <DialogSaveDiagramVue ref="dialogSave"></DialogSaveDiagramVue>
    <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>

    <v-card :elevation="3" class="explorer">
      <v-card-actions>
        <v-btn icon="mdi-home" class="explorerBtn" :disabled="deleteActive" @click="homeButtonClick"></v-btn>
        <v-btn icon="mdi-content-save" class="explorerBtn" :disabled="deleteActive" @click="saveDialogButtonClick"></v-btn>
        <v-btn icon="mdi-delete" class="explorerBtn" :class="{ deleteBtnActive: deleteActive }" @click="deleteActive = !deleteActive"></v-btn>
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
  //displayDiagrams.value.length = 0;

  let userId = authUserStore.auth.user?.id;
  if (userId != undefined) {
    updateFiles(userId);
  } else {
    console.log("userId is undefined");
  }
});

const updateFiles = (uId: number) => {
  return new Promise<void>((resolve, reject) => {
    diagramService
      .getCategories(uId)
      .then((categories) => {
        diagramService
          .getDiagramsByUserId(uId)
          .then((userDiagrams) => {
            map.value = diagramService.getDiagramsWithCategory(categories.data, userDiagrams.data);
            displayDiagrams.value = [];
            if (activeCategorie.value != null) {
              map.value.get(activeCategorie.value)?.forEach((diagram) => {
                displayDiagrams.value.push(diagram);
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

const homeButtonClick = () => {
  categoriesViewActive.value = true;
  updateFiles(authUserStore.auth.user?.id as number);
};

const categoryClicked = (category: Category) => {
  if (deleteActive.value) {
    if (dialogConfirm.value) {
      dialogConfirm.value.openDialog(`Lösche: ${category.name}`, "Willst du die Kategorie wirklich löschen? Wenn du sie löscht, werden auch alle Diagramme gelöscht, die in dieser Kategorie sind.").then((result: boolean) => {
        if (result) {
          diagramService.deleteCategory(category).then(() => {
            updateFiles(authUserStore.auth.user?.id as number);
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
            // TODO: Reload doesnt work
            updateFiles(authUserStore.auth.user?.id as number).then(() => {
              selectCategory(activeCategorie.value as Category);
            });
          });
        }
      });
    }
  }
};

const saveDialogButtonClick = () => {
  dialogSave.value?.openDialog(activeDiagramId.value).then((result: boolean) => {
    if (result) {
      updateFiles(authUserStore.auth.user?.id as number);
    }
  });
  categoryNames.value.length = 0;
};
</script>

<style scoped lang="scss">
.explorer {
  z-index: 1;
  position: absolute;
  padding: 10px;
  max-width: 200px;
  min-height: 200px;
}

.explorerBtn {
  height: 30px;
  border-radius: 3px;

  &:hover {
    margin-bottom: 5px;
    * {
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
