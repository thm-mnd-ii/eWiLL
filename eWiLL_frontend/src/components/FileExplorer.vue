<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <div class="explorer">
    <v-btn prepend-icon="mdi-home" class="explorerBtn" @click="homeButtonClick">Home</v-btn>
    <v-btn icon="mdi-content-save" class="explorerBtn" @click="saveButtonClick">
      <v-dialog v-model="dialog" activator="parent">
        <v-card>
          <v-card-title>
            <span class="text-h5">Save Diagram</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12" sm="6" md="4">
                  <v-text-field label="Name*" required></v-text-field>
                </v-col>
                <v-col cols="12" sm="6">
                  <v-select :items="categorieNames" label="Kategorie*" required></v-select>
                </v-col>
                <v-col>
                  <v-btn cols="12" sm="6" md="4" icon="mdi-home"></v-btn>
                </v-col>
              </v-row>
            </v-container>
            <small>*indicates required field</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn variant="text" @click="dialog = false"> Close </v-btn>
            <v-btn variant="text" @click="dialog = false"> Save </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-btn>
    <v-list v-if="categoryActive">
      <v-list-item v-for="[key] in map" :key="key" class="list-item" :value="key" @click="categoryClicked(key)">
        <template #prepend>
          <v-icon :icon="IconFolder" size="13px"></v-icon>
        </template>
        <v-list-item-title v-text="key"></v-list-item-title>
      </v-list-item>
    </v-list>
    <v-list>
      <v-list-item v-for="diagram in displayDiagrams" :key="diagram.name" class="list-item" :value="diagram.name" @click="diagramClicked(diagram)">
        <template #prepend>
          <v-icon :icon="IconFile" size="13px"></v-icon>
        </template>
        <v-list-item-title v-text="diagram.name"></v-list-item-title>
      </v-list-item>
    </v-list>
  </div>
</template>

<script setup lang="ts">
import Diagram from "../model/diagram/Diagram";
import IconFolder from "../components/icons/IconFolder.vue";
import IconFile from "../components/icons/IconFile.vue";

import { onMounted, onBeforeMount, reactive, ref } from "vue";
import DiagramType from "../enums/DiagramType";
import diagramService from "../services/diagram.service";
import Category from "../model/diagram/Category";
import { useStore } from "vuex";
import Entity from "../model/diagram/Entity";
import Connection from "../model/diagram/Connection";
import Config from "../model/diagram/Config";

const store = useStore();
const categoryActive = ref(true);
const dialog = ref(false);
const activeCategorie = ref("");
var activeDiagram = {} as Diagram;
const userId = ref(0);

var modelID = ref(0);
var displayDiagrams: Diagram[] = reactive([]);
var categories: Category[] = reactive([]);
var categorieNames: String[] = ["test", "test2", "test3"];
var map: Map<string, Diagram[]> = reactive(new Map());

const emit = defineEmits(["update:entity", "anker-point", "delete-entity", "change-entity-typ", "manage-attributes"]);
//const updateEntity = ref(updateCurrentEntity.value)

const props = defineProps<{
  entities: Entity[];
  ankerpoints: Connection[];
}>();

const homeButtonClick = () => {
  categoryActive.value = true;
  displayDiagrams.length = 0;
  map.get("Keine Kategorie")?.forEach((diagram) => {
    displayDiagrams.push(diagram);
  });
};

const categoryClicked = (category: string) => {
  categoryActive.value = false;
  activeCategorie.value = category;
  displayDiagrams.length = 0;
  map.get(category)?.forEach((diagram) => {
    displayDiagrams.push(diagram);
  });
};

const saveButtonClick = () => {
  const tmpDiagram = {} as Diagram;
  tmpDiagram.id = 0;
  tmpDiagram.ownerId = userId.value;
  tmpDiagram.name = "Test";
  tmpDiagram.connections = props.ankerpoints;
  tmpDiagram.entities = props.entities;

  const category = {} as Category;
  category.name = "1";
  category.userId = 1;

  const config = {} as Config;
  config.id = 1;
  config.diagramType = DiagramType.SERM;

  tmpDiagram.config = config;
};

const diagramClicked = (diagram: Diagram) => {
  activeDiagram = diagram;
};

onBeforeMount(() => {
  map = diagramService.getDiagramsWithCategory(1);
  categories = diagramService.getCategories(1);
  userId.value = store.state.auth.userId;
});

onMounted(() => {
  setDefaultDiagram();
});

const setDefaultDiagram = () => {
  const tmpDiagram = {} as Diagram;
  tmpDiagram.id = 0;
  tmpDiagram.ownerId = userId.value;
  tmpDiagram.entities = props.entities;
  tmpDiagram.connections = props.ankerpoints;
  tmpDiagram.name = "Default Name";

  const tmpConfig = {} as Config;
  tmpConfig.id = 1;
  tmpConfig.diagramType = DiagramType.SERM;
  tmpDiagram.config = tmpConfig;

  const tmpCategory = {} as Category;
  tmpCategory.id = 1;
  tmpCategory.name = "Keine Kategorie";
  tmpCategory.userId = userId.value;
  tmpDiagram.category = tmpCategory;

  activeDiagram = tmpDiagram;
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
}
</style>
