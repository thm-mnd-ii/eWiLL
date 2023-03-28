<template>
  <v-card elevation="0">
    <v-card-title> </v-card-title>
    <v-card-text>
      <div class="toolbox">
        <IconEntity id="item" draggable="true" @click="addElement($event, EntityType.ENTITY)" />
        <IconRelationshiptyp @mousedown="addElement($event, EntityType.RELATIONSHIP)" />
        <IconEntityRelationshiptyp @mousedown="addElement($event, EntityType.ENTITYRELATIONSHIP)" />
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import { useDiagramStore } from "@/stores/diagramStore";

import IconEntity from "@/components/icons/IconEntitytyp.vue";
import IconRelationshiptyp from "@/components/icons/IconRelationshiptyp.vue";
import IconEntityRelationshiptyp from "@/components/icons/IconEntityRelationshiptyp.vue";

import EntityType from "@/enums/EntityType";

const diagramStore = useDiagramStore();

//add Element with serial ID
const addElement = (e: { clientY: number; clientX: number }, type: EntityType) => {
  if (diagramStore.diagram.entities.length == 0) {
    diagramStore.diagram.entities.push({ id: 1, type: type, entityName: "New Entity", attributes: [], top: e.clientY - 100, left: e.clientX - 50, width: 100 });
    diagramStore.saveHistory();
    return;
  }

  const ids = diagramStore.diagram.entities.map((entity: { id: any }) => {
    return entity.id;
  });
  const max = Math.max(...ids);
  const nextID = max + 1;

  diagramStore.diagram.entities.push({ id: nextID, type: type, entityName: "New Entity", attributes: [], top: e.clientY - 100, left: e.clientX - 50, width: 100 });

  diagramStore.saveHistory();
};
</script>

<style scoped lang="scss">
.toolbox {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr;
  grid-gap: 10px;

  * {
    cursor: copy;
  }
}
</style>
