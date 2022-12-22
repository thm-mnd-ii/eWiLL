<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <div class="explorer">
    <v-btn prepend-icon="mdi-home" @click="homeButtonClick">Home</v-btn>
    <v-list v-if="categoryActive">
      <v-list-item v-for="[key] in map" :key="key" class="list-item" :value="key" @click="categoryClicked(key)">
        <template #prepend>
          <v-icon :icon="IconFolder" size="13px"></v-icon>
        </template>
        <v-list-item-title v-text="key"></v-list-item-title>
      </v-list-item>
    </v-list>
    <v-list>
      <v-list-item v-for="diagram in displayDiagrams" :key="diagram.name" class="list-item" :value="diagram.name">
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

import { onBeforeMount, reactive, ref } from "vue";
import DiagramType from "../enums/DiagramType";
import diagramService from "../services/diagram.service";
import Category from "../model/diagram/Category";
import { useStore } from "vuex";
import Entity from "../model/diagram/Entity";
import Connection from "../model/diagram/Connection";

const store = useStore();

const test = ref(false);
const categoryActive = ref(true);
var modelID = ref(0);
var displayDiagrams: Diagram[] = reactive([]);
var map: Map<string, Diagram[]> = reactive(new Map());

const emit = defineEmits(["update:entity", "anker-point", "delete-entity", "change-entity-typ", "manage-attributes"]);
//const updateEntity = ref(updateCurrentEntity.value)

const props = defineProps<{
  entitys: Entity[];
  ankerpoints: Connection[];
}>();

const homeButtonClick = () => {
  //diagrams.splice(0);
  //diagrams.push(diagrams2.values);
  //DiagramService.test();
  categoryActive.value = true;
  displayDiagrams.length = 0;
};

const categoryClicked = (category: string) => {
  categoryActive.value = false;
  displayDiagrams.length = 0;
  map.get(category)?.forEach((diagram) => {
    displayDiagrams.push(diagram);
  });
};

onBeforeMount(() => {
  map = diagramService.getDiagramsWithCategory(1);
});
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

.v-btn {
  margin-bottom: 10px;
  padding: 2px;
  max-height: 25px;
}
</style>
