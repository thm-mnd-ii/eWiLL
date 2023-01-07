<template>
  <div ref="root" class="objectContainer" @mouseover="hover = true" @mouseleave="endHover">
    <span v-if="!isEditable" class="text unselectable" @dblclick="makeTextEditable">{{ entity.entityName }}</span>
    <textarea v-if="isEditable" v-model="entity.entityName" class="textedit" rows="1" @dblclick="makeTextEditable" @keyup.enter="handleEnter"></textarea>

    <!-- eslint-disable vue/no-v-html -->
    <span class="attributes unselectable" v-html="formattedAttributes"></span>
    <!--eslint-enable-->

    <!-- <div v-if="isResizable" @mousedown="resizer($event)" class="resizer nw"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer ne"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer sw"></div> -->
    <div v-if="isResizable" class="resizer se" @mousedown="resizer($event)"></div>

    <!-- <AnkerPoint v-if="hover && !isResizable" position="top" :entityWidth="props.entity.width" @ankerPosition="handleAnkerPoint"></AnkerPoint> -->
    <!-- <AnkerPoint v-if="hover && !isResizable" position="left" :entity-width="props.entity.width" @anker-position="handleAnkerPoint"></AnkerPoint> -->
    <!-- <AnkerPoint v-if="hover && !isResizable" position="right" :entity-width="props.entity.width" @anker-position="handleAnkerPoint"></AnkerPoint> -->
    <!-- <AnkerPoint v-if="hover && !isResizable" position="bottom" :entityWidth="props.entity.width" @ankerPosition="handleAnkerPoint"></AnkerPoint> -->
    <div v-if="hover && !isResizable">
      <OutgoingAnkerPoint v-for="anker in outgoingAnkerPoint" :key="anker" :position="anker" :entity-width="props.entity.width" @anker-position="handleAnkerPoint" />
    </div>

    <div v-if="hover && !isResizable">
      <IncomingAnkerPoint v-for="anker in incomingAnkerPoint" :key="anker" :position="anker" :entity-width="props.entity.width" @anker-position="handleAnkerPoint" />
    </div>
    <EntityWidget v-if="isResizable" :entity="props.entity" />

    <IconEntity v-if="props.entity.type == EntityTyp.ENTITY" class="entity" @dblclick="activateEntity()" @mousedown="mousedown($event)" />
    <IconRelationshiptyp v-if="props.entity.type == EntityTyp.RELATIONSHIP" class="entity" @dblclick="activateEntity()" @mousedown="mousedown($event)" />
    <IconEntityRelationshiptyp v-if="props.entity.type == EntityTyp.ENTITYRELATIONSHIP" class="entity" @dblclick="activateEntity()" @mousedown="mousedown($event)" />
  </div>
</template>

<script setup lang="ts">
import IconEntityRelationshiptyp from "../icons/IconEntityRelationshiptyp.vue";
import IconEntity from "../icons/IconEntitytyp.vue";
import IconRelationshiptyp from "../icons/IconRelationshiptyp.vue";
import EntityTyp from "../../enums/EntityType";
import AttributeTyp from "../../enums/AttributeType";
import OutgoingAnkerPoint from "./OutgoingAnkerPoint.vue";
import IncomingAnkerPoint from "./IncomingAnkerPoint.vue";
import EntityWidget from "./EntityWidget.vue";

import { ref, onMounted, computed, watch } from "vue";
import { useDiagramStore } from "../../stores/diagramStore";
import { useToolManagementStore } from "../../stores/toolManagementStore";

import ConnectorPosition from "../../enums/ConnectorPosition";
import Attribute from "../../model/diagram/Attribute";
import Entity from "../../model/diagram/Entity";

const emit = defineEmits(["anker-point"]);
//const updateEntity = ref(updateCurrentEntity.value)

const props = defineProps<{
  entity: Entity;
}>();

onMounted(() => {
  setAnkerPoints();
  setPosition(root.value, props.entity);
  updateAttributes();
});

// Emit new entity to parent
watch(props.entity, () => {
  setAnkerPoints();
  setPosition(root.value, props.entity);
  updateAttributes();
});

const toolManagementStore = useToolManagementStore();
const diagramStore = useDiagramStore();
let entity = diagramStore.diagram.entities.find((entity) => entity.id == props.entity.id);
const root = ref<HTMLInputElement | null>(null);

const cssVarAttributesDistanceTop = computed(() => {
  return props.entity.width / 2 + "px";
});

const formattedAttributes = ref("");
//const attributes = ref(props.entity.attributes)
const updateAttributes = () => {
  //clear
  formattedAttributes.value = "";

  props.entity.attributes.forEach((attribute: Attribute) => {
    switch (attribute.type) {
      case AttributeTyp.PrimaryKey:
        formattedAttributes.value += "<b>" + attribute.name + "</b>, ";
        break;

      case AttributeTyp.ForeignKey:
        formattedAttributes.value += "<u>" + attribute.name + "</u>, ";
        break;

      case AttributeTyp.Attribute:
        formattedAttributes.value += "<i>" + attribute.name + "</i>, ";
        break;

      default:
        throw "Not implemented Attribute Typ";
    }
  });

  // cut off ", "
  formattedAttributes.value = formattedAttributes.value.slice(0, -2);
};

const handleAnkerPoint = (ankerPosition: ConnectorPosition) => {
  // console.log(e)
  // console.log(props.entity.id)
  emit("anker-point", { id: props.entity.id, position: ankerPosition });
};

const hover = ref<boolean>(false);
// watch(hover, (e) => {
//     console.log(`Hover: ${e}`)
// })

const endHover = () => {
  setTimeout(() => {
    hover.value = false;
  }, 1000);
};

const isEditable = ref<boolean>(false);
const makeTextEditable = () => {
  isEditable.value = !isEditable.value;
};

const handleEnter = (e: any) => {
  if (entity?.entityName !== undefined) {
    //console.log(e);
    const curPos = e.srcElement.selectionStart;

    if (e.ctrlKey) {
      const textarea = entity?.entityName;
      //use return to exit methode
      return (entity.entityName = textarea.slice(0, curPos) + "\n" + textarea.slice(curPos));
    }
    entity.entityName = entity?.entityName.slice(0, curPos - 1) + props.entity.entityName.slice(curPos);
    makeTextEditable();
  }
};

const outgoingAnkerPoint = ref<ConnectorPosition[]>([]);
const incomingAnkerPoint = ref<ConnectorPosition[]>([]);

const setAnkerPoints = () => {
  outgoingAnkerPoint.value = [];
  incomingAnkerPoint.value = [];

  switch (props.entity.type) {
    case EntityTyp.ENTITY:
      outgoingAnkerPoint.value.push(ConnectorPosition.Right);
      break;
    case EntityTyp.RELATIONSHIP:
      incomingAnkerPoint.value.push(ConnectorPosition.Left);
      break;
    case EntityTyp.ENTITYRELATIONSHIP:
      outgoingAnkerPoint.value.push(ConnectorPosition.Right);
      incomingAnkerPoint.value.push(ConnectorPosition.Left);
      break;

    default:
      throw "EntityTyp not defined";
  }
};

const setPosition = (element: HTMLInputElement | null, entity: Entity) => {
  if (element != null) {
    element.style.top = entity.top + "px";
    element.style.left = entity.left + "px";
    element.style.width = entity.width + "px";
  }
};

const updateEntity = () => {
  if (root.value != null && entity != undefined) {
    entity.top = parseInt(root.value.style.top);
    entity.left = parseInt(root.value.style.left);
    entity.width = parseInt(root.value.style.width);
  }
};

const activateEntity = () => {
  if (entity != undefined) {
    if (toolManagementStore.selectedEntity == entity) {
      toolManagementStore.selectedEntity = null;
    } else toolManagementStore.selectedEntity = entity;
  }
};

const isResizable = computed(() => {
  return toolManagementStore.selectedEntity === entity;
});

const mousedown = (e: any) => {
  //console.log(root.value.parentNode.getBoundingClientRect())

  console.log("move");
  let el = e.target.parentNode;

  if (root.value == null || root.value.parentElement == null) return;
  let container = root.value.parentElement;

  let prevX = e.clientX;
  let prevY = e.clientY;
  //console.log(`Current mouse position: X ${prevX} | Y  ${prevX}`)

  const mousemove = (e: any) => {
    let newX = prevX - e.clientX;
    let newY = prevY - e.clientY;
    //console.log(`NEW client position: X ${newX} | Y  ${newY}`)

    const rect = el.getBoundingClientRect();
    const rectParent = container.getBoundingClientRect();

    //calculare position relative to container
    let relativePos: { top?: number; right?: number; bottom?: number; left?: number } = {};
    relativePos.top = rect.top - rectParent.top;
    relativePos.right = rect.right - rectParent.right;
    relativePos.bottom = rect.bottom - rectParent.bottom;
    relativePos.left = rect.left - rectParent.left;

    let newLeft = relativePos.left - newX;
    let newTop = relativePos.top - newY;

    //console.log(`NEW Position: X ${newLeft} | Y  ${newTop}`)

    //set boundaries
    if (newLeft >= 0 && newTop >= 0 && newLeft <= rectParent.width - rect.width && newTop <= rectParent.height - rect.height) {
      el.style.left = newLeft + "px";
      el.style.top = newTop + "px";
    }

    //console.log(`NEW Element position: X ${el.style.left} | Y  ${el.style.top}`)

    prevX = e.clientX;
    prevY = e.clientY;
  };

  const mouseup = () => {
    window.removeEventListener("mousemove", mousemove);
    window.removeEventListener("mouseup", mouseup);

    //Update Entity to Send new coordinates to parent component
    updateEntity();
  };

  window.addEventListener("mousemove", mousemove);
  window.addEventListener("mouseup", mouseup);
};

const resizer = (e: any) => {
  console.log("resize");

  let currentResizer = e.target;

  let el = e.target.parentNode;

  if (root.value == null || root.value.parentElement == null) return;
  let container = root.value.parentElement;

  let prevX = e.clientX;
  let prevY = e.clientY;

  const mousemove = (e: { clientX: number; clientY: number }) => {
    const rect = el.getBoundingClientRect();
    const rectParent = container.getBoundingClientRect();

    //calculare position relative to container
    let relativePos: { top?: number; right?: number; bottom?: number; left?: number } = {};
    relativePos.top = rect.top - rectParent.top;
    relativePos.right = rect.right - rectParent.right;
    relativePos.bottom = rect.bottom - rectParent.bottom;
    relativePos.left = rect.left - rectParent.left;

    // TODO: set boundaries
    // if ((newLeft>=0) && (newTop>=0) && (newLeft<=rectParent.width-rect.width) && (newTop<=rectParent.height-rect.height)) {
    //     el.style.left = newLeft + 'px'
    //     el.style.top = newTop + 'px'
    // }

    if (currentResizer.classList.contains("nw")) {
      console.log("nw");
      el.style.width = rect.width + (prevX - e.clientX) + "px";
      el.style.left = relativePos.left + (prevX - e.clientX) + "px";
      el.style.top = relativePos.top - (prevY - e.clientY) + "px";
      el.style.left = relativePos.left - (prevX - e.clientX) + "px";
      // TODO: Bug
    } else if (currentResizer.classList.contains("ne")) {
      console.log("ne");
      el.style.width = rect.width - (prevX - e.clientX) + "px";
      el.style.top = relativePos.top - (prevY - e.clientY) + "px";
      // TODO: Bug
    } else if (currentResizer.classList.contains("sw")) {
      console.log("sw");
      el.style.width = rect.width + (prevX - e.clientX) + "px";
      el.style.left = relativePos.left - (prevX - e.clientX) + "px";
    } else if (currentResizer.classList.contains("se")) {
      console.log("se");
      el.style.width = rect.width - (prevX - e.clientX) + "px";
    }

    prevX = e.clientX;
    prevY = e.clientY;
  };

  const mouseup = () => {
    window.removeEventListener("mousemove", mousemove);
    window.removeEventListener("mouseup", mouseup);

    //Update Entity to Send new coordinates to parent component
    updateEntity();
  };

  window.addEventListener("mousemove", mousemove);
  window.addEventListener("mouseup", mouseup);
};
</script>

<style scoped lang="scss">
.entity {
  pointer-events: all;
  * {
    pointer-events: none;
  }
}
.objectContainer {
  z-index: 5;
  position: absolute;
  cursor: move;
}

.resizer {
  position: absolute;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background-color: #00abe3;
  z-index: 10;
}

.resizer.nw {
  top: -2px;
  left: -2px;
  cursor: nw-resize;
}

.resizer.ne {
  top: -2px;
  right: -2px;
  cursor: ne-resize;
}

.resizer.sw {
  bottom: -2px;
  left: -2px;
  cursor: sw-resize;
}

.resizer.se {
  bottom: 4px;
  right: -2px;
  cursor: se-resize;
}

.text {
  min-width: 10px;
  min-height: 10px;

  text-align: center;
  display: inline-block;
  font-size: smaller;
  position: absolute;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
  cursor: text;
}

.textedit {
  text-align: center;
  resize: none;
  overflow: hidden;
  width: 94%;
  height: 80%;
  position: absolute;
  font-size: smaller;
  top: 44%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
}

.unselectable {
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

.attributes {
  font-size: x-small;
  text-align: left;
  vertical-align: text-top;

  position: absolute;

  top: v-bind("cssVarAttributesDistanceTop");
  cursor: default;
}
</style>
