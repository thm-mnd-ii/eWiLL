<template>
  <!-- <div>
        {{calculatedLine}}
    </div> -->

  <!-- <div>
    {{ newAnkerPoint }}
  </div> -->

  <!-- <div>
    {{ lineList }}
  </div> -->

  <!-- <div>
        {{entityList}}
    </div> -->

  <!-- <div>{{ selectedEntity }}</div> -->

  <div class="container">
    <ModalAddAttributes />
    <div>
      <FileExplorer></FileExplorer>
    </div>
    <div class="toolbox">
      <IconEntity id="item" draggable="true" @click="addElement($event, EntityType.ENTITY)" />
      <IconRelationshiptyp @mousedown="addElement($event, EntityType.RELATIONSHIP)" />
      <IconEntityRelationshiptyp @mousedown="addElement($event, EntityType.ENTITYRELATIONSHIP)" />
    </div>

    <div class="modellingContainer" @click.self="unselectAll">
      <EntityMain v-for="entity in diagramStore.diagram.entities" :key="entity.id" :entity="entity" @anker-point="handleAnkerPoint" />

      <LineMain v-for="line in lineList" :key="line.id" :line="line" />

      <!-- Definiert global das aussehen der Pfeile (TODO: In Component auslagern) -->
      <svg class="svgMarker">
        <defs>
          <marker id="arrowhead" markerWidth="20" markerHeight="10" refX="5" refY="2" orient="auto">
            <polygon points="0 0, 0 4, 6 2" />
          </marker>

          <filter id="double">
            <feMorphology in="SourceGraphic" result="a" operator="dilate" radius="1.2" />
            <feComposite in="SourceGraphic" in2="a" result="xx" operator="xor" />
          </filter>
        </defs>
      </svg>
    </div>
  </div>
</template>

<script setup lang="ts">
import EntityMain from "../components/EntityMain.vue";
import LineMain from "../components/LineMain.vue";
import ModalAddAttributes from "../dialog/DialogAddAttributes.vue";
import FileExplorer from "../components/FileExplorer.vue";

import IconEntityRelationshiptyp from "../components/icons/IconEntityRelationshiptyp.vue";
import IconEntity from "../components/icons/IconEntitytyp.vue";
import IconRelationshiptyp from "../components/icons/IconRelationshiptyp.vue";

import { onMounted, reactive, ref } from "vue";
import { useDiagramStore } from "../stores/diagramStore";
import { useToolManagementStore } from "../stores/toolManagementStore";

import EntityType from "../enums/EntityType";
import Connection from "../model/diagram/Connection";
import Line from "../model/diagram/Line";

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const lineList: Line[] = reactive([
  //{ "id": 1, "x1": 200, "y1": 100, "x2": 0, "y2": 0},
  //{ "id": 2, "x1": 200, "y1": 100, "x2": 0, "y2": 200},
]);

const newAnkerPoint = ref<Connection>({});

onMounted(() => {
  updateLines();
});

diagramStore.$subscribe(() => {
  updateLines();
});

const unselectAll = () => {
  toolManagementStore.selectedEntity = null;
  toolManagementStore.selectedLine = null;
};

const updateLines = () => {
  let calculatedLines: Line[] = [];

  diagramStore.diagram.connections.forEach((connection: Connection, index) => {
    let calculatedLine = calculateLine(connection);
    if (calculatedLine != undefined) {
      calculatedLine.id = index;
      calculatedLines.push(calculatedLine);
    }
  });

  //replace lineList with new calculatedLines
  lineList.splice(0, lineList.length);
  lineList.push(...calculatedLines);
};

const calculateLine = (connection: Connection): Line | undefined => {
  let line: Line = {} as Line;

  let startEntity = diagramStore.diagram.entities.find((entity: { id: number }) => entity.id == connection.startEntity);
  let endEntity = diagramStore.diagram.entities.find((entity: { id: number }) => entity.id == connection.endEntity);

  // if startEntity or endEntity is undefined, return empty line
  if (startEntity === undefined || endEntity === undefined) return;

  let startEntityWidth: number = startEntity.width;
  let startPositionFactor = getPositionFactor(connection.startEntityPosition, startEntityWidth);

  line.y1 = startEntity.top + startPositionFactor.y;
  line.x1 = startEntity.left + startPositionFactor.x;

  let endEntityWidth = endEntity.width;
  let endPositionFactor = getPositionFactor(connection.endEntityPosition, endEntityWidth);
  line.y2 = endEntity.top + endPositionFactor.y;
  line.x2 = endEntity.left + endPositionFactor.x;

  line.style = connection.style;

  return line;
};

const getPositionFactor = (position: any, entityWidth: number) => {
  let positionFactor = { x: 0, y: 0 };

  switch (position) {
    case "top":
      positionFactor.y = 0;
      positionFactor.x = entityWidth / 2;
      break;
    case "right":
      positionFactor.y = entityWidth / 4;
      positionFactor.x = entityWidth;
      break;
    case "bottom":
      positionFactor.y = entityWidth / 2;
      positionFactor.x = entityWidth / 2;
      break;
    case "left":
      positionFactor.y = entityWidth / 4;
      positionFactor.x = 0;
      break;
    default:
      throw "Unhandled position from anker.";
  }

  return positionFactor;
};

let triggered = false;
const handleAnkerPoint = (ankerPoint: { id: any; position: any }) => {
  if (!triggered) {
    triggered = true;
    newAnkerPoint.value.startEntity = ankerPoint.id;
    newAnkerPoint.value.startEntityPosition = ankerPoint.position;
  } else if (triggered) {
    triggered = false;
    newAnkerPoint.value.endEntity = ankerPoint.id;
    newAnkerPoint.value.endEntityPosition = ankerPoint.position;

    //initialize style with 0
    newAnkerPoint.value.style = 0;

    diagramStore.diagram.connections.push(newAnkerPoint.value);
    newAnkerPoint.value = {};
  }
};

//add Element with serial ID
const addElement = (e: { clientY: number; clientX: number }, type: EntityType) => {
  if (diagramStore.diagram.entities.length == 0) {
    diagramStore.diagram.entities.push({ id: 1, type: type, entityName: "New Entity", attributes: [], top: e.clientY - 100, left: e.clientX - 50, width: 100 });
    return;
  }

  const ids = diagramStore.diagram.entities.map((entity: { id: any }) => {
    return entity.id;
  });
  const max = Math.max(...ids);
  const nextID = max + 1;

  diagramStore.diagram.entities.push({ id: nextID, type: type, entityName: "New Entity", attributes: [], top: e.clientY - 100, left: e.clientX - 50, width: 100 });
};
</script>

<style scoped lang="scss">
.container {
  position: relative;
  width: 100%;
  height: 100vh;
  margin-bottom: 5%;
}
.modellingContainer {
  position: relative;
  background-color: lavender;
  left: 15%;
  top: 10%;
  width: 1000px;
  height: 500px;
  z-index: 1;
}

.toolbox {
  z-index: 1;
  position: absolute;
  top: 400px;
  left: 30px;
  padding: 10px;
  width: 140px;
  height: auto;
  border-radius: 5px;
  box-shadow: 4px 4px 10px 0px rgba(15, 33, 47, 0.421);
}

.toolbox > * {
  margin: 10px;
  cursor: copy;
}

.svgMarker {
  width: 0px;
  height: 0px;
}
</style>
