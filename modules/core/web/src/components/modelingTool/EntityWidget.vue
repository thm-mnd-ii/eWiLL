<template>
  <div class="widgetContainer">
    <span>
      <v-icon class="widget" icon="mdi-chevron-left" color="info" @click="previousEntityType"></v-icon>
      <v-tooltip activator="parent" location="top">Vorheriger Typ</v-tooltip>
    </span>
    <span>
      <v-icon class="widget" icon="mdi-chevron-right" color="info" @click="nextEntityType"></v-icon>
      <v-tooltip activator="parent" location="top">Nächster Typ</v-tooltip>
    </span>
    <span>
      <v-icon v-if="!showEntityTypes" class="widget" icon="mdi-arrow-collapse-down" size="x-small" color="info" @click="showEntityTypes = true"></v-icon>
      <v-tooltip v-if="!showEntityTypes" activator="parent" location="top">Wähle aus Liste</v-tooltip>
      <v-icon v-if="showEntityTypes" class="widget" icon="mdi-arrow-expand-up" size="x-small" color="info" @click="showEntityTypes = false"></v-icon>
      <v-tooltip v-if="showEntityTypes" activator="parent" location="top">Schließen</v-tooltip>
    </span>
    <span v-if="showEntityTypes">
      <v-btn class="widget" color="info" variant="text" @click="setEntityType(EntityType.ENTITY)">E</v-btn>
    </span>
    <span v-if="showEntityTypes">
      <v-btn class="widget" color="info" variant="text" @click="setEntityType(EntityType.ENTITYRELATIONSHIP)">ER</v-btn>
    </span>
    <span v-if="showEntityTypes">
      <v-btn class="widget" color="info" variant="text" @click="setEntityType(EntityType.RELATIONSHIP)">R</v-btn>
    </span>

    <span>
      <v-icon v-if="!showEntityTypes" class="widget" icon="mdi-pencil-outline" color="success" @click="manageAttributes"></v-icon>
      <v-tooltip activator="parent" location="top">Attribute</v-tooltip>
    </span>

    <span>
      <v-icon v-if="!showEntityTypes" class="widget" icon="mdi-delete-outline" color="error" @click="deleteEntity"></v-icon>
      <v-tooltip activator="parent" location="top">Delete</v-tooltip>
    </span>
  </div>
</template>

<script setup lang="ts">
import { useDiagramStore } from "../../stores/diagramStore";
import { useToolManagementStore } from "../../stores/toolManagementStore";

import Entity from "../../model/diagram/Entity";
import EntityType from "../../enums/EntityType";
import { ref } from "vue";

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const props = defineProps<{
  entity: Entity;
}>();

const showEntityTypes = ref(false);

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

const previousEntityType = () => {
  let entityIndex = diagramStore.diagram.entities.indexOf(props.entity);
  const currentTyp = diagramStore.diagram.entities[entityIndex].type;

  if (currentTyp === EntityType.ENTITY) {
    diagramStore.diagram.entities[entityIndex].type = 2;
  } else {
    diagramStore.diagram.entities[entityIndex].type = currentTyp - 1;
  }

  diagramStore.saveHistory();
};

const nextEntityType = () => {
  let entityIndex = diagramStore.diagram.entities.indexOf(props.entity);
  const currentTyp = diagramStore.diagram.entities[entityIndex].type;

  if (currentTyp === EntityType.RELATIONSHIP) {
    diagramStore.diagram.entities[entityIndex].type = 0;
  } else {
    diagramStore.diagram.entities[entityIndex].type = currentTyp + 1;
  }

  diagramStore.saveHistory();
};

const setEntityType = (entityType: EntityType) => {
  let entityIndex = diagramStore.diagram.entities.indexOf(props.entity);
  diagramStore.diagram.entities[entityIndex].type = entityType;

  diagramStore.saveHistory();
};

const manageAttributes = () => {
  // console.log("manageAttributes");
  toolManagementStore.selectedEntity = props.entity;
  toolManagementStore.showModalAddAttributes = true;
};
</script>

<style scoped lang="scss">
.widgetContainer {
  position: absolute;
  top: -60px;

  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}
.widget {
  width: 33px;
  height: 25px;
  padding: 2px;
  margin: 2px;

  min-width: 0;

  border-radius: 5px;
  cursor: pointer;
  background-color: rgb(255, 255, 255);
}
</style>
