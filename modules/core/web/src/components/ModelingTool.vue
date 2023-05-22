<template>
  <div class="container" :class="{ 'not-clickable': !props.isEditable }">
    <ModalAddAttributes />
    <div ref="modelingContainer" class="modelingContainer" @click.self="unselectAll">
      <EntityMain v-for="entity in entities.diagram.value.entities" :key="entity.id" class="entity" :entity="entity" :is-editable="props.isEditable" />

      <LineMain v-for="line in lineList" :key="line.id" :line="line" />

      <!-- Definiert global das aussehen der Pfeile -->
      <ArrowDefinitionVue class="svgMarker"></ArrowDefinitionVue>
    </div>
  </div>
</template>

<script setup lang="ts">
import EntityMain from "./modelingTool/EntityMain.vue";
import LineMain from "./modelingTool/LineMain.vue";

import ArrowDefinitionVue from "./modelingTool/ArrowDefinition.vue";
import ModalAddAttributes from "../dialog/DialogAddAttributes.vue";

import { onMounted, reactive, computed, ref } from "vue";
import { useDiagramStore } from "../stores/diagramStore";
import { useToolManagementStore } from "../stores/toolManagementStore";
import { storeToRefs } from "pinia";

import Connection from "../model/diagram/Connection";
import Line from "../model/diagram/Line";

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

// props
const props = defineProps<{
  isEditable: boolean;
}>();

const modelingContainer = ref<HTMLElement | null>(null);

const lineList: Line[] = reactive([]);

const entities = storeToRefs(diagramStore);

const minWidth = computed(() => diagramStore.getMinWidth() + 200);
const minHeight = computed(() => diagramStore.getMinHeight() + 200);

// const newAnkerPoint = ref<Connection>({});

onMounted(() => {
  updateLines();
  updateArea();

  toolManagementStore.diagramDiv = modelingContainer.value as HTMLElement;
});

diagramStore.$subscribe(() => {
  updateLines();
  updateArea();
  setTimeout(() => {
    diagramStore.saved = false;
  }, 2000);
});

const unselectAll = () => {
  toolManagementStore.selectedEntity = null;
  toolManagementStore.selectedLine = null;
  toolManagementStore.resetConnection();
};

const updateArea = () => {
  if (modelingContainer.value == null) return;

  modelingContainer.value.style.width = minWidth.value + "px";
  modelingContainer.value.style.height = minHeight.value + "px";
};

const updateLines = () => {
  // TODO: find a better way to check if connections is undefined
  if (diagramStore.diagram.connections === undefined) return;

  let calculatedLines: Line[] = [];

  diagramStore.diagram.connections.forEach((connection: Connection, index) => {
    let calculatedLine = calculateLine(connection);
    if (calculatedLine != undefined) {
      calculatedLine.id = index;
      calculatedLines.push(calculatedLine);
    }
  });

  //spread duplicate lines
  calculatedLines = spreadDuplicateLines(calculatedLines);

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

  let startEntityWidth = startEntity.width;
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

const spreadDuplicateLines = (calculatedLines: Line[]) => {
  // TODO: refactor for better readability. Combine duplicateStartEntitys and duplicateEndEntitys and use a function to check if the entity is a start or end entity

  // show duplicate start entitys which start at the same position
  let duplicateStartEntitys = diagramStore.diagram.connections.filter((connection: Connection) => {
    return (
      diagramStore.diagram.connections.filter((connection2: Connection) => {
        return connection.startEntity == connection2.startEntity && connection.startEntityPosition == connection2.startEntityPosition;
      }).length > 1
    );
  });

  // show duplicate end entitys which end at the same position
  let duplicateEndEntitys = diagramStore.diagram.connections.filter((connection: Connection) => {
    return (
      diagramStore.diagram.connections.filter((connection2: Connection) => {
        return connection.endEntity == connection2.endEntity && connection.endEntityPosition == connection2.endEntityPosition;
      }).length > 1
    );
  });

  //group duplicate start entitys by startEntity and startEntityPosition without using reduce
  let groupedDuplicateStartEntitys = [] as Connection[][];
  duplicateStartEntitys.forEach((connection: Connection) => {
    let group = groupedDuplicateStartEntitys.find((group: Connection[]) => {
      return group[0].startEntity == connection.startEntity && group[0].startEntityPosition == connection.startEntityPosition;
    });
    if (group != undefined) {
      group.push(connection);
    } else {
      groupedDuplicateStartEntitys.push([connection]);
    }
  });

  //group duplicate end entitys by endEntity and endEntityPosition without using reduce
  let groupedDuplicateEndEntitys = [] as Connection[][];
  duplicateEndEntitys.forEach((connection: Connection) => {
    let group = groupedDuplicateEndEntitys.find((group: Connection[]) => {
      return group[0].endEntity == connection.endEntity && group[0].endEntityPosition == connection.endEntityPosition;
    });
    if (group != undefined) {
      group.push(connection);
    } else {
      groupedDuplicateEndEntitys.push([connection]);
    }
  });

  // calculate offset for each duplicate 10px
  groupedDuplicateStartEntitys.forEach((group: Connection[]) => {
    group.forEach((connection: Connection, index2) => {
      const sumOfLines = group.length;

      let calculatedLine = calculatedLines.find((line: Line) => {
        return line.id == diagramStore.diagram.connections.indexOf(connection);
      });
      if (calculatedLine != undefined && calculatedLine.y1 != undefined) {
        // spread arround y1
        calculatedLine.y1 = calculatedLine.y1 + (index2 - (sumOfLines - 1) / 2) * 10;
      }
    });
  });

  // calculate offset for each duplicate 10px
  groupedDuplicateEndEntitys.forEach((group: Connection[]) => {
    group.forEach((connection: Connection, index2) => {
      const sumOfLines = group.length;

      let calculatedLine = calculatedLines.find((line: Line) => {
        return line.id == diagramStore.diagram.connections.indexOf(connection);
      });
      if (calculatedLine != undefined && calculatedLine.y2 != undefined) {
        // spread arround y2
        calculatedLine.y2 = calculatedLine.y2 + (index2 - (sumOfLines - 1) / 2) * 10;
      }
    });
  });

  return calculatedLines;
};
</script>

<style scoped lang="scss">
// center the modeling container
.container {
  width: 100px;
  height: 100px;
  background-color: lavender;
  z-index: 0;
  overflow: auto;
}

.modelingContainer {
  position: relative;
  min-width: 500px;
  min-height: 500px;
  max-width: 2000px;
  max-height: 2000px;
  background-color: lavender;
  z-index: 1;
}

.toolbox > * {
  margin: 10px;
  cursor: copy;
}

.svgMarker {
  width: 0px;
  height: 0px;
}

.not-clickable * {
  pointer-events: none;
  cursor: default;
  z-index: 10;
}
</style>
