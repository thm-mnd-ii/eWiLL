<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <div class="explorer">
    <v-btn prepend-icon="mdi-home" class="explorerBtn" :disabled="deleteActive" @click="homeButtonClick">Home</v-btn>
    <v-btn id="saveBtn" :disabled="deleteActive" class="explorerBtn" @click="saveDialogButtonClick">
      <v-icon>mdi-content-save</v-icon>
      <v-dialog v-model="saveDialog" activator="parent">
        <v-card>
          <v-card-title>
            <span class="text-h5">Save Diagram</span>
            <br />
            <span v-if="!newDiagram" class="text-subtitle-1"
              >{{ saveNameProp }} will be overwritten!
              <v-btn id="saveAsNewBtn" @click="(newDiagram = true), (putChangedToPost = true)">Save as new Diagram</v-btn>
            </span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12" sm="6" md="4">
                  <v-text-field v-model="saveNameProp" label="Name*" required :disabled="newDiagram == false"></v-text-field>
                </v-col>
                <v-col cols="12" sm="6">
                  <v-select v-model="saveCategoryProp" :items="categoryNames" label="Kategorie*" required :disabled="newDiagram == false"></v-select>
                </v-col>
                <v-col>
                  <v-btn cols="12" sm="6" md="4" icon="mdi-folder-plus" :disabled="newDiagram == false">
                    <v-icon>mdi-folder-plus</v-icon>
                    <v-dialog v-model="createCategoryDialog" activator="parent" width="500px">
                      <v-card>
                        <v-card-title>
                          <span class="text-h5">Create Category</span>
                        </v-card-title>
                        <v-card-text>
                          <v-container>
                            <v-row>
                              <v-text-field v-model="createCategoryName" label="Name*" required></v-text-field>
                            </v-row>
                          </v-container>
                        </v-card-text>
                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn variant="text" @click="createCategoryDialog = false"> Close </v-btn>
                          <v-btn variant="text" @click="createCategoryClick"> Erstellen </v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
            <small>*indicates required field</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn variant="text" @click="closeSaveDialog"> Close </v-btn>
            <v-btn variant="text" @click="finalSaveClick"> Save </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-btn>
    <v-btn :class="deleteActive ? 'deleteBtnActive' : 'deleteBtnInactive'" @click="deleteActive = !deleteActive">
      <v-icon>mdi-delete</v-icon>
      <v-dialog v-model="deleteDialog" width="500px">
        <v-card>
          <v-card-title>
            <span class="text-h5">Delete: {{ deleteProp }}</span
            ><br />
            <span v-if="categoriesViewActive" class="text-subtitle-1">Category and all containing diagrams will be deleted</span>
          </v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn variant="text" @click="deleteDialog = false"> Close </v-btn>
            <v-btn variant="text" @click="finalDeleteClick"> Delete </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-btn>
    <v-list v-if="categoriesViewActive">
      <v-list-item v-for="[key] in map" :key="key" class="list-item" @click="categoryClicked(key)">
        <template #prepend>
          <v-icon size="15px">mdi-folder</v-icon>
        </template>
        <v-list-item-title v-text="key"></v-list-item-title>
      </v-list-item>
    </v-list>
    <v-list>
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
import diagramService from "../services/diagram.service";
import { useDiagramStore } from "../stores/diagramStore";
import { useAuthUserStore } from "../stores/authUserStore";
import { onMounted, ref } from "vue";

const categoriesViewActive = ref(true);
const activeCategorie = ref("");
var activeDiagram = {} as Diagram;
const userId = ref(0);
// Constants holding diagrams and categories
var categories: Category[] = [];
const categoryNames = ref<string[]>([]);
const map = ref<Map<string, Diagram[]>>(new Map<string, Diagram[]>());
const displayDiagrams = ref<Diagram[]>([]);
//Constants for saving dialog
const saveDialog = ref(false);
const putChangedToPost = ref(false);
const saveNameProp = ref("");
const saveCategoryProp = ref("");
const newDiagram = ref(true);
// Constants for delete dialog
const deleteDialog = ref(false);
const deleteActive = ref(false);
const deleteProp = ref("");
const deleteItemCategory = ref();
const deleteItemDiagram = ref();
// Constants for category dialog
const createCategoryDialog = ref(false);
const createCategoryName = ref("");

const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();
const ALERT_UPS = "Ups, something went wrong";
const ALERT_SAVE = "Diagramm konnte nicht gespeichert werden";

const homeButtonClick = () => {
  categoriesViewActive.value = true;
  displayDiagrams.value.length = 0;
};

const categoryClicked = (category: string) => {
  if (deleteActive.value) {
    deleteDialog.value = true;
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
  activeDiagram = diagram;
  newDiagram.value = false;
  saveCategoryProp.value = diagram.category.name;
  saveNameProp.value = diagram.name;
  diagramStore.diagram = diagram;
};

const diagramSingleClick = (diagram: Diagram) => {
  if (deleteActive.value) {
    deleteDialog.value = true;
    deleteProp.value = diagram.name;
    deleteItemDiagram.value = diagram;
    deleteItemCategory.value = null;
  }
};

const saveDialogButtonClick = () => {
  categoryNames.value.length = 0;
  categories.forEach((category) => {
    categoryNames.value.push(category.name);
  });
};

const closeSaveDialog = () => {
  saveDialog.value = false;
  if (putChangedToPost.value == true) {
    newDiagram.value = false;
    putChangedToPost.value = false;
  }
};

const finalSaveClick = () => {
  activeDiagram.name = saveNameProp.value;
  activeDiagram.ownerId = userId.value;
  categories.forEach((category) => {
    if (category.name == saveCategoryProp.value) {
      activeDiagram.category.id = category.id;
      activeDiagram.category.name = category.name;
      activeDiagram.category.userId = category.userId;
    }
  });
  saveDialog.value = false;
  if (newDiagram.value == true) {
    diagramService
      .postDiagram(activeDiagram)
      .then((response) => {
        activeDiagram.id = response.data;
        newDiagram.value = false;
        reload();
      })
      .catch(() => {
        alert(ALERT_SAVE);
      });
  } else {
    diagramService
      .putDiagram(activeDiagram)
      .then(() => {
        reload();
      })
      .catch(() => {
        alert(ALERT_SAVE);
      });
  }
};

const finalDeleteClick = () => {
  if (deleteItemDiagram.value != null) {
    diagramService
      .deleteDiagram(deleteItemDiagram.value)
      .then(() => {
        reload();
        deleteDialog.value = false;
      })
      .catch(() => {
        alert(ALERT_UPS);
      });
  } else if (deleteItemCategory.value != null) {
    const categoryToDelete = searchForCategory(deleteItemCategory.value);
    if (categoryToDelete.id == null) {
      alert(ALERT_UPS);
    } else {
      diagramService
        .deleteCategory(categoryToDelete)
        .then(() => {
          reload();
          deleteDialog.value = false;
        })
        .catch(() => {
          alert(ALERT_UPS);
        });
    }
  }
};

onMounted(() => {
  displayDiagrams.value.length = 0;
  var tmpUserId = authUserStore.auth.user?.id;
  if (tmpUserId != null) userId.value = tmpUserId;
  reload();
  activeDiagram = diagramStore.diagram;
  const category = {} as Category;
  activeDiagram.category = category;
});

const createCategoryClick = () => {
  diagramService
    .postCategory(createCategoryName.value, userId.value)
    .then((response) => {
      if (response.status == 200) {
        categoryNames.value.push(createCategoryName.value);
        createCategoryDialog.value = false;
        reload();
      }
    })
    .catch((error) => {
      console.log(error);
      alert(ALERT_UPS);
    });
};

const reload = () => {
  diagramService
    .getCategories(userId.value)
    .then((response) => {
      categories = response;
      diagramService
        .getDiagramsByUserId(userId.value)
        .then((response) => {
          map.value = diagramService.getDiagramsWithCategory(categories, response);
          displayDiagrams.value.length = 0;
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

const searchForCategory = (categoryName: string): Category => {
  const tmpCategory = {} as Category;
  categories.forEach((category) => {
    if (category.name == categoryName) {
      tmpCategory.id = category.id;
      tmpCategory.name = category.name;
      tmpCategory.userId = category.userId;
      return tmpCategory;
    }
  });
  return tmpCategory;
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
  margin-bottom: 10px;
  padding: 2px;
  max-height: 25px;
  margin: 2px;
}

#saveBtn {
  border-radius: 4px;
  max-width: 30px !important;
  min-width: 0px;
}

.deleteBtnInactive {
  margin-bottom: 10px;
  padding: 2px;
  max-height: 25px;
  margin: 2px;
  border-radius: 4px;
  max-width: 30px !important;
  min-width: 0px;
}

.deleteBtnActive {
  margin-bottom: 10px;
  padding: 2px;
  max-height: 25px;
  margin: 2px;
  border-radius: 4px;
  max-width: 30px !important;
  min-width: 0px;
  background-color: red;
}

#saveAsNewBtn {
  padding: 2px;
  margin-left: 10px;
}
</style>
