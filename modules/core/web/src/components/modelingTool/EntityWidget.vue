<template>
  <div class="widgetContainer">
    <span>
      <v-icon class="widget" icon="mdi-pencil-outline" color="success" @click="manageAttributes"></v-icon>
      <v-tooltip activator="parent" location="top">Attribute</v-tooltip>
    </span>
    <span>
      <v-icon class="widget" icon="mdi-swap-horizontal" color="info" @click="changeEntityType"></v-icon>
      <v-tooltip activator="parent" location="top">Change Type</v-tooltip>
    </span>
    <span>
      <v-icon class="widget" icon="mdi-delete-outline" color="error" @click="deleteEntity"></v-icon>
      <v-tooltip activator="parent" location="top">Delete</v-tooltip>
    </span>
  </div>
</template>

<script setup lang="ts">
import { useDiagramStore } from "../../stores/diagramStore";
import { useToolManagementStore } from "../../stores/toolManagementStore";

import Entity from "../../model/diagram/Entity";
import EntityType from "../../enums/EntityType";

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const props = defineProps<{
  entity: Entity;
}>();

const deleteEntity = () => {
  // delete Entity
  let entityIndex = diagramStore.diagram.entities.indexOf(props.entity);
  diagramStore.diagram.entities.splice(entityIndex, 1);

  // diese Lösung killt den Watcher
  // entityList.value = entityList.value.filter(entity => entity.id == entityToDelete.id)

  // finde and merge all relations
  let relationsWithStartEntity = diagramStore.diagram.connections.filter((line) => line.startEntity == props.entity.id);
  let relationsWithEndEntity = diagramStore.diagram.connections.filter((line) => line.endEntity == props.entity.id);
  let relationsToDelete = relationsWithStartEntity.concat(relationsWithEndEntity);

  // delete relations
  for (const relation of relationsToDelete) {
    let relationIndex = diagramStore.diagram.connections.indexOf(relation);
    diagramStore.diagram.connections.splice(relationIndex, 1);
  }

  diagramStore.saveHistory();
};

const changeEntityType = () => {
  let entityIndex = diagramStore.diagram.entities.indexOf(props.entity);
  const currentTyp = diagramStore.diagram.entities[entityIndex].type;

  if (currentTyp === EntityType.RELATIONSHIP) {
    diagramStore.diagram.entities[entityIndex].type = 0;
  } else {
    diagramStore.diagram.entities[entityIndex].type = currentTyp + 1;
  }

  diagramStore.saveHistory();
};

const manageAttributes = () => {
  console.log("manageAttributes");
  toolManagementStore.selectedEntity = props.entity;
  toolManagementStore.showModalAddAttributes = true;
};
</script>

<style scoped lang="scss">
.widgetContainer {
  position: absolute;
  top: -35px;
}
.widget {
  width: 25px;
  height: 25px;
  padding: 2px;
  margin: 2px;

  border-radius: 5px;
  cursor: pointer;
  background-color: rgb(255, 255, 255);
}
</style>
