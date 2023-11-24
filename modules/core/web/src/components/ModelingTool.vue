<template>
  <div class="container" :class="{ 'not-clickable': !props.isEditable }" @click.right="showContextMenu">
    <ModalAddAttributes />
    <div ref="modelingContainer" class="modelingContainer" @click.self="unselectAll">
      <EntityMain v-for="entity in entities.diagram.value.entities" :key="entity.id" class="entity" :entity="entity" :is-editable="props.isEditable" />
      <LineMain v-for="line in lineList" :key="line.id" :line="line" />
      <!-- Definiert global das aussehen der Pfeile -->
      <ArrowDefinitionVue class="svgMarker"></ArrowDefinitionVue>

      <!-- Context menu -->
      <QuickInsertion :is-visible="isContextMenuVisible" :position="contextMenuPosition" :modeling-container="modelingContainer" @close-menu="isContextMenuVisible = false" />
    </div>
  </div>
</template>

<script setup lang="ts">
import EntityMain from "./modelingTool/EntityMain.vue";
import LineMain from "./modelingTool/LineMain.vue";
import ArrowDefinitionVue from "./modelingTool/ArrowDefinition.vue";
import ModalAddAttributes from "../dialog/DialogAddAttributes.vue";
import QuickInsertion from "./modelingTool/QuickInsertion.vue";
import { ref, onMounted, reactive } from "vue";
import { useDiagramStore } from "../stores/diagramStore";
import { useToolManagementStore } from "../stores/toolManagementStore";
import Connection from "../model/diagram/Connection";
import Line from "../model/diagram/Line";
import { defineProps } from "vue";
import { storeToRefs } from "pinia";

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

// props
const props = defineProps<{
  isEditable: boolean;
}>();

const isContextMenuVisible = ref(false);

const contextMenuPosition = ref({ x: "0px", y: "0px" });
const modelingContainer = ref<HTMLElement | null>(null);

// Add this to store the right-click position
let rightClickPosition = reactive({ x: 0, y: 0 });

const lineList: Line[] = reactive([]);

const entities = storeToRefs(diagramStore);

onMounted(() => {
  updateLines();
  updateArea();
  toolManagementStore.diagramDiv = modelingContainer.value as HTMLElement;
  if (modelingContainer.value != null) {
    modelingContainer.value.addEventListener("contextmenu", (ev) => {
      ev.preventDefault();
    });
  }

  // Close the context menu when clicking anywhere else except the menu
  window.addEventListener("click", (event) => {
    closeContextMenu(event);
  });
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

  const minWidth = diagramStore.getMinWidth() + 200;
  const minHeight = diagramStore.getMinHeight() + 200;

  modelingContainer.value.style.width = minWidth + "px";
  modelingContainer.value.style.height = minHeight + "px";
};

const updateLines = () => {
  // TODO: find a better way to check if connections are undefined
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
      throw "Unhandled position from anchor.";
  }

  return positionFactor;
};

const spreadDuplicateLines = (calculatedLines: Line[]) => {
  // TODO: refactor for better readability. Combine duplicatedStartEntitys and duplicateEndEntitys and use a function to check if the entity is a start or end entity

  // show duplicate start entities which start at the same position
  let duplicatedStartEntities = diagramStore.diagram.connections.filter((connection: Connection) => {
    return (
      diagramStore.diagram.connections.filter((connection2: Connection) => {
        return connection.startEntity == connection2.startEntity && connection.startEntityPosition == connection2.startEntityPosition;
      }).length > 1
    );
  });

  // show duplicate end entities which end at the same position
  let duplicatedEndEntities = diagramStore.diagram.connections.filter((connection: Connection) => {
    return (
      diagramStore.diagram.connections.filter((connection2: Connection) => {
        return connection.endEntity == connection2.endEntity && connection.endEntityPosition == connection2.endEntityPosition;
      }).length > 1
    );
  });

  //group duplicate start entities by startEntity and startEntityPosition without using reduce
  let groupedDuplicatedStartEntities = [] as Connection[][];
  duplicatedStartEntities.forEach((connection: Connection) => {
    let group = groupedDuplicatedStartEntities.find((group: Connection[]) => {
      return group[0].startEntity == connection.startEntity && group[0].startEntityPosition == connection.startEntityPosition;
    });
    if (group != undefined) {
      group.push(connection);
    } else {
      groupedDuplicatedStartEntities.push([connection]);
    }
  });

  //group duplicate end entities by endEntity and endEntityPosition without using reduce
  let groupedDuplicatedEndEntities = [] as Connection[][];
  duplicatedEndEntities.forEach((connection: Connection) => {
    let group = groupedDuplicatedEndEntities.find((group: Connection[]) => {
      return group[0].endEntity == connection.endEntity && group[0].endEntityPosition == connection.endEntityPosition;
    });
    if (group != undefined) {
      group.push(connection);
    } else {
      groupedDuplicatedEndEntities.push([connection]);
    }
  });

  // calculateGradients
  groupedDuplicatedStartEntities = calculateGradients(groupedDuplicatedStartEntities, calculatedLines);
  groupedDuplicatedEndEntities = calculateGradients(groupedDuplicatedEndEntities, calculatedLines);

  // order groupedDuplicatedStartEntities by gradient
  groupedDuplicatedStartEntities.forEach((group: Connection[]) => {
    group.sort((a: Connection, b: Connection) => {
      if (a.gradient == undefined || b.gradient == undefined) return 0;
      return a.gradient - b.gradient;
    });
  });

  // order groupedDuplicatedEndEntities by gradient
  groupedDuplicatedEndEntities.forEach((group: Connection[]) => {
    group.sort((a: Connection, b: Connection) => {
      if (a.gradient == undefined || b.gradient == undefined) return 0;
      return b.gradient - a.gradient;
    });
  });

  // calculate offset for each duplicate 10px
  groupedDuplicatedStartEntities.forEach((group: Connection[]) => {
    group.forEach((connection: Connection, index2) => {
      const sumOfLines = group.length;

      let calculatedLine = calculatedLines.find((line: Line) => {
        return line.id == diagramStore.diagram.connections.indexOf(connection);
      });
      if (calculatedLine != undefined && calculatedLine.y1 != undefined) {
        // spread around y1
        calculatedLine.y1 = calculatedLine.y1 + (index2 - (sumOfLines - 1) / 2) * 10;
      }
    });
  });

  // calculate offset for each duplicate 10px
  groupedDuplicatedEndEntities.forEach((group: Connection[]) => {
    group.forEach((connection: Connection, index2) => {
      const sumOfLines = group.length;

      let calculatedLine = calculatedLines.find((line: Line) => {
        return line.id == diagramStore.diagram.connections.indexOf(connection);
      });
      if (calculatedLine != undefined && calculatedLine.y2 != undefined) {
        // spread around y2
        calculatedLine.y2 = calculatedLine.y2 + (index2 - (sumOfLines - 1) / 2) * 10;
      }
    });
  });

  return calculatedLines;
};

const calculateGradients = (groupedEntities: Connection[][], calculatedLines: Line[]) => {
  groupedEntities.forEach((group: Connection[]) => {
    group.forEach((connection: Connection) => {
      let calculatedLine = calculatedLines.find((line: Line) => {
        return line.id == diagramStore.diagram.connections.indexOf(connection);
      });
      if (calculatedLine != undefined && calculatedLine.y1 != undefined && calculatedLine.y2 != undefined && calculatedLine.x1 != undefined && calculatedLine.x2 != undefined) {
        // calculate the slope of the line
        connection.gradient = (calculatedLine.y2 - calculatedLine.y1) / (calculatedLine.x2 - calculatedLine.x1);
      }
    });
  });

  return groupedEntities;
};

const showContextMenu = (event: MouseEvent) => {
  if (toolManagementStore.$state.selectedEntity === null) {
    isContextMenuVisible.value = true;
    rightClickPosition.x = event.clientX;
    rightClickPosition.y = event.clientY;
    contextMenuPosition.value = { x: `${event.clientX}px`, y: `${event.clientY}px` };
  }

  event.preventDefault(); // Prevent the default context menu
};

// Close the context menu when clicking anywhere else
const closeContextMenu = (event: MouseEvent) => {
  if (!isContextMenuVisible.value) return;

  const contextMenu = document.querySelector(".context-menu");
  if (contextMenu === null) return;

  if (!contextMenu.contains(event.target as Node)) {
    isContextMenuVisible.value = false;
  }
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
