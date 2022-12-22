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
    <ModalAddAttributes :show="showModalAddAttributes" :entity="selectedEntity" @close="showModalAddAttributes = false" />
    <div>
      <FileExplorer :entities="entityList" :ankerpoints="ankerPoints"></FileExplorer>
    </div>
    <div class="toolbox">
      <IconEntity id="item" draggable="true" @click="addElement($event, EntityType.ENTITY)" />
      <IconRelationshiptyp @mousedown="addElement($event, EntityType.RELATIONSHIP)" />
      <IconEntityRelationshiptyp @mousedown="addElement($event, EntityType.ENTITYRELATIONSHIP)" />
    </div>

    <div class="modellingContainer">
      <EntityMain v-for="entity in entityList" :key="entity.id" :entity="entity" @anker-point="handleAnkerPoint" @delete-entity="deleteEntity" @change-entity-typ="changeEntityType" @manage-attributes="manageAttributes" />

      <LineMain v-for="line in lineList" :key="line.id" :line="line" @delete-line="deleteLine" @change-line="changeLineStyle" />

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
import ModalAddAttributes from "../components/ModalAddAttributes.vue";
import FileExplorer from "../components/FileExplorer.vue";

import IconEntityRelationshiptyp from "../components/icons/IconEntityRelationshiptyp.vue";
import IconEntity from "../components/icons/IconEntitytyp.vue";
import IconRelationshiptyp from "../components/icons/IconRelationshiptyp.vue";

import EntityType from "../enums/EntityType";

import { onMounted, reactive, ref, watch } from "vue";
import Entity from "../model/diagram/Entity";
import AttributeType from "../enums/AttributeType";
import Connection from "../model/diagram/Connection";
import Line from "../model/diagram/Line";
import CardinalityType from "../enums/CardinalityType";
import ConnectorPosition from "../enums/ConnectorPosition";

const entityList: Entity[] = reactive([
  {
    id: 1,
    type: EntityType.ENTITY,
    entityName: "Kunde",
    top: 124,
    left: 81,
    width: 100,
    attributes: [
      { name: "KNr", type: AttributeType.PrimaryKey },
      { name: "Adresse", type: AttributeType.Attribute },
      { name: "Name", type: AttributeType.Attribute },
      { name: "Vorname", type: AttributeType.Attribute },
    ],
  },
  {
    id: 2,
    type: EntityType.ENTITYRELATIONSHIP,
    entityName: "Rechnung",
    top: 122,
    left: 302,
    width: 100,
    attributes: [
      { name: "RNr", type: AttributeType.PrimaryKey },
      { name: "KNr", type: AttributeType.ForeignKey },
      { name: "Datum", type: AttributeType.Attribute },
    ],
  },
  {
    id: 3,
    type: EntityType.ENTITY,
    entityName: "Artikel",
    top: 207,
    left: 81,
    width: 100,
    attributes: [],
  },
  {
    id: 4,
    type: EntityType.RELATIONSHIP,
    entityName: "Rechnungs\npositionen",
    top: 118,
    left: 486,
    width: 100,
    attributes: [],
  },
]);

const lineList: Line[] = reactive([
  //{ "id": 1, "x1": 200, "y1": 100, "x2": 0, "y2": 0},
  //{ "id": 2, "x1": 200, "y1": 100, "x2": 0, "y2": 200},
  // { "id": 3}
]);

const newAnkerPoint = ref<Connection>({});

const ankerPoints: Connection[] = reactive([
  { startEntity: 1, startEntityPosition: ConnectorPosition.Right, endEntity: 2, endEntityPosition: ConnectorPosition.Left, style: CardinalityType.ONE_TO_MANY },
  { startEntity: 3, startEntityPosition: ConnectorPosition.Right, endEntity: 2, endEntityPosition: ConnectorPosition.Left, style: CardinalityType.ZERO_TO_MANY },
  { startEntity: 2, startEntityPosition: ConnectorPosition.Right, endEntity: 4, endEntityPosition: ConnectorPosition.Left, style: CardinalityType.ZERO_TO_ONE },
]);

onMounted(() => {
  updateLines();
});

watch(
  entityList,
  () => {
    console.log("watcher: entityList");
    updateLines();
  },
  { deep: true }
);

watch(
  ankerPoints,
  () => {
    console.log("watcher: ankerPoints");
    updateLines();
  },
  { deep: true }
);

const updateLines = () => {
  let calculatedLines: Line[] = [];

  console.log("Update Lines");
  //console.log(ankerPoints)

  for (const [index, anker] of ankerPoints.entries()) {
    let calculatedLine = calculateLine(anker);
    if (calculatedLine != undefined) {
      calculatedLine.id = index;
      calculatedLines.push(calculatedLine);
    }
  }

  //replace lineList with new calculatedLines
  lineList.splice(0, lineList.length);
  lineList.push(...calculatedLines);
};

const calculateLine = (anker: Connection): Line | undefined => {
  let line: Line = {} as Line;

  let startEntity = entityList.find((entity: { id: number }) => entity.id == anker.startEntity);
  let endEntity = entityList.find((entity: { id: number }) => entity.id == anker.endEntity);

  // if startEntity or endEntity is undefined, return empty line
  if (startEntity === undefined || endEntity === undefined) return;

  let startEntityWidth: number = startEntity.width;
  let startPositionFactor = getPositionFactor(anker.startEntityPosition, startEntityWidth);

  line.y1 = startEntity.top + startPositionFactor.y;
  line.x1 = startEntity.left + startPositionFactor.x;

  let endEntityWidth = endEntity.width;
  let endPositionFactor = getPositionFactor(anker.endEntityPosition, endEntityWidth);
  line.y2 = endEntity.top + endPositionFactor.y;
  line.x2 = endEntity.left + endPositionFactor.x;

  line.style = anker.style;

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

const deleteEntity = (entityToDelete: Entity) => {
  // delete Entity
  let entityIndex = entityList.indexOf(entityToDelete);
  entityList.splice(entityIndex, 1);

  // diese Lösung killt den Watcher
  // entityList.value = entityList.value.filter(entity => entity.id == entityToDelete.id)

  // finde and merge all relations
  let relationsWithStartEntity = ankerPoints.filter((line) => line.startEntity == entityToDelete.id);
  let relationsWithEndEntity = ankerPoints.filter((line) => line.endEntity == entityToDelete.id);
  let relationsToDelete = relationsWithStartEntity.concat(relationsWithEndEntity);

  // delete relations
  for (const relation of relationsToDelete) {
    let relationIndex = ankerPoints.indexOf(relation);
    ankerPoints.splice(relationIndex, 1);
  }
};

const changeEntityType = (entity: Entity) => {
  let entityIndex = entityList.indexOf(entity);
  const currentTyp = entityList[entityIndex].type;
  console.log(currentTyp);

  if (currentTyp === EntityType.RELATIONSHIP) {
    entityList[entityIndex].type = 0;
  } else {
    entityList[entityIndex].type = currentTyp + 1;
  }
};

const showModalAddAttributes = ref(false);
const selectedEntity = ref<Entity | undefined>(undefined);

const manageAttributes = (entity: Entity) => {
  selectedEntity.value = entity;
  showModalAddAttributes.value = true;
};

const deleteLine = (lineToDelete: Line) => {
  // aus der Array-Position der Linie lässt sich auch der ankerpunkt bestimmen
  // damit nicht nur die linie, sondern auch die beziehung gelöscht wird, muss der Ankerpunkt gelöscht werden
  let lineIndex = lineList.indexOf(lineToDelete);
  ankerPoints.splice(lineIndex, 1);
};

const changeLineStyle = (line: Line) => {
  let lineIndex = lineList.indexOf(line);
  const currentStyle = ankerPoints[lineIndex].style;
  if (currentStyle != undefined) {
    if (currentStyle == CardinalityType.ONE_TO_ONE) {
      ankerPoints[lineIndex].style = 0;
    } else {
      ankerPoints[lineIndex].style = currentStyle + 1;
    }
  }
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

    ankerPoints.push(newAnkerPoint.value);
    newAnkerPoint.value = {};
  }

  console.log(triggered);
};

//add Element with serial ID
const addElement = (e: { clientY: number; clientX: number }, type: EntityType) => {
  if (entityList.length == 0) {
    entityList.push({ id: 1, type: type, entityName: "New Entity", attributes: [], top: e.clientY - 100, left: e.clientX - 50, width: 100 });
    return;
  }

  const ids = entityList.map((entity: { id: any }) => {
    return entity.id;
  });
  const max = Math.max(...ids);
  const nextID = max + 1;

  entityList.push({ id: nextID, type: type, entityName: "New Entity", attributes: [], top: e.clientY - 100, left: e.clientX - 50, width: 100 });
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
