<template>
  
 <div class="container" :class="{ 'not-clickable': !props.isEditable }" @contextmenu="showContextMenu">
    <ModalAddAttributes />
    <div ref="modelingContainer" class="modelingContainer" @click.self="unselectAll">
      <EntityMain v-for="entity in entities.diagram.value.entities" :key="entity.id" class="entity" :entity="entity" :is-editable="props.isEditable" />
      <LineMain v-for="line in lineList" :key="line.id" :line="line" />
      <!-- Definiert global das aussehen der Pfeile -->
      <ArrowDefinitionVue class="svgMarker"></ArrowDefinitionVue>
      <!-- Context menu -->
      <div  v-if="isContextMenuVisible" class="context-menu" :style="{ left: contextMenuPosition.x, top: contextMenuPosition.y }">
       
      

    <v-card  width="165.167">
      <template v-slot:title>
        Elemente <v-icon icon="mdi-menu-down"></v-icon>
      </template>

      <v-list>
  <v-list-item class="custom-list-item">
    <v-list-item-title>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="52.9586 182.183 61.91 31.75" @mousedown="addElement($event, EntityType.ENTITY)" >
        <path d="m53.058594 182.2832v0.70118 30.85156h61.705076v-31.55274h-61.705076zm1.40039 1.40039h58.904296v28.75196h-58.904296v-28.75196z" fill="#000000" />
  </svg>
    </v-list-item-title>
  </v-list-item>
  <v-list-item class="custom-list-item">
    <v-list-item-title>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="87.3453 830.214 233.4 119.5" @mousedown="addElement($event, EntityType.RELATIONSHIP)" >
        <path
      d="m90.087891 830.31445-2.642579 2.65039v113.96094l2.642579 2.64453h227.923829l2.64258-2.64453v-113.96094l-2.64258-2.65039h-227.923829zm2.65039 5.29297h47.482419l-47.482419 48.14844v-48.14844zm54.919919 0h112.51758l53.83594 54.58985-54.08203 54.08007h-112.02539l-54.087894-54.08007 53.841794-54.58985zm119.94922 0h47.76172v48.43164l-47.76172-48.43164zm47.76172 60.7168v47.95312h-47.95312l47.95312-47.95312zm-222.630859 0.27344 47.679689 47.67968h-47.679689v-47.67968z"
      fill="#000000"
    />
      </svg>
    </v-list-item-title>
  </v-list-item>
  <v-list-item class="custom-list-item">
    <v-list-item-title>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="141.89 718.091 233.4 119.5" @mousedown="addElement($event, EntityType.ENTITYRELATIONSHIP)" >
        <path d="m141.99023 718.19141v2.64257 116.61133h233.2168v-119.2539h-233.2168zm5.29297 5.29297h47.7461l-47.7461 48.4082v-48.4082zm55.17774 0h167.45312v108.66796h-167.72656l-53.81836-53.82031 54.0918-54.84765zm-55.17774 61.24804 47.41992 47.41992h-47.41992v-47.41992z" fill="#000000" />
  </svg>
    </v-list-item-title>
  </v-list-item>
</v-list>
    </v-card>

       
        
       
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import EntityMain from "./modelingTool/EntityMain.vue";
import LineMain from "./modelingTool/LineMain.vue";
import ArrowDefinitionVue from "./modelingTool/ArrowDefinition.vue";
import ModalAddAttributes from "../dialog/DialogAddAttributes.vue";
import { ref } from "vue";
import { onMounted, reactive } from "vue";
import { useDiagramStore } from "../stores/diagramStore";
import { useToolManagementStore } from "../stores/toolManagementStore";
import Connection from "../model/diagram/Connection";
import Line from "../model/diagram/Line";
import { defineProps } from "vue";
import { storeToRefs } from "pinia";
import EntityType from "@/enums/EntityType";


// props
const props = defineProps<{
  isEditable: boolean;
}>();

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();
const isContextMenuVisible = ref(false);
const contextMenuPosition = ref({ x: '0px', y: '0px' });
const modelingContainer = ref<HTMLElement | null>(null);
// Add this to store the right-click position
let rightClickPosition = { x: 0, y: 0 };
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

const addElement = (e: MouseEvent, type: EntityType) => {
  const container = modelingContainer.value;
  if (!container) return; // Make sure the container is available.

  
  // Calculate the position relative to the container.
  const positionX = rightClickPosition.x - container.getBoundingClientRect().left + container.scrollLeft;
  const positionY = rightClickPosition.y - container.getBoundingClientRect().top + container.scrollTop;

  if (diagramStore.diagram.entities.length === 0) {
    diagramStore.diagram.entities.push({
      id: 1,
      type: type,
      entityName: "New Entity",
      attributes: [],
      top: positionY,
      left: positionX,
      width: 100
    });
    diagramStore.saveHistory();
    return;
  }

  const ids = diagramStore.diagram.entities.map((entity: { id: any }) => {
    return entity.id;
  });
  const max = Math.max(...ids);
  const nextID = max + 1;

  diagramStore.diagram.entities.push({
    id: nextID,
    type: type,
    entityName: "New Entity",
    attributes: [],
    top: positionY,
    left: positionX,
    width: 100
  });

  diagramStore.saveHistory();
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
  event.preventDefault();
  if (toolManagementStore.$state.selectedEntity === null) {
    isContextMenuVisible.value = true;
    contextMenuPosition.value = { x: event.clientX + 'px', y: event.clientY + 'px' };
    
    // Store the right-click position
    rightClickPosition = { x: event.clientX, y: event.clientY };
  }
};
// Close the context menu when clicking anywhere else
const closeContextMenu = () => {
  if (isContextMenuVisible.value) {
    isContextMenuVisible.value = false;
  }
};

onMounted(() => {
  window.addEventListener("click", closeContextMenu);
});

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

.context-menu {
  position: fixed;
  background-color: white;
  border: 1px solid #ccc;
  z-index: 1000;
  min-width: 100px;
  max-width: 200px;
  box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.2);
  padding: 8px;
  cursor: pointer;
}

.context-menu div {
  padding: 5px;
  cursor: pointer;
}


.custom-list-item {
  width: 120px; 
 
}
.custom-list-item:hover {
  background-color: #f0f0f0; /* Background color when hovered */
  /* Add any other styles you want to change on hover */
}
svg {
  pointer-events: all;
  * {
    pointer-events: none !important;
  }
}
.context-menu-item {
  padding: 4px 0;
  transition: background-color 0.2s;
  cursor: pointer;
}

.context-menu-item:hover {
  background-color: #f0f0f0; /* Background color when hovered */
}

</style>
