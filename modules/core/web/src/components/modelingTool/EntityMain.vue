<template>
  <div ref="root" class="objectContainer" :class="{ active: isActive, highlighted: isEntityHighlighted }" @mouseover="props.isEditable ? (hover = true) : null" @mouseleave="hover = false">
    <div v-if="hover" class="click-area"></div>
    <span v-if="!isTextEditable" class="text unselectable" @dblclick="makeTextEditable"> {{ entity.entityName }}</span>
    <textarea v-if="isTextEditable" v-model="entity.entityName" class="textedit" rows="1" @dblclick="makeTextEditable" @keyup.enter="handleEnter"></textarea>

    <!-- eslint-disable vue/no-v-html -->
    <span class="attributes unselectable" v-html="formattedAttributes"></span>
    <!--eslint-enable-->

    <!-- <div v-if="isResizable" @mousedown="resizer($event)" class="resizer nw"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer ne"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer sw"></div> 
        <div v-if="isResizable" class="resizer se" @mousedown="resizer($event)"></div> -->

    <div v-if="hover && !isSelected && !toolManagementStore.showIncomingAnkerPoints">
      <OutgoingAnkerPoint v-for="anker in outgoingAnkerPoint" :key="anker" :position="anker" :entity="props.entity" />
    </div>

    <div v-if="toolManagementStore.showIncomingAnkerPoints && toolManagementStore.newConnection.startEntity != props.entity.id">
      <IncomingAnkerPoint v-for="anker in incomingAnkerPoint" :key="anker" :position="anker" :entity="props.entity" />
    </div>
    <EntityWidget v-if="isSelected" :entity="props.entity" />

    <IconEntity v-if="props.entity.type == EntityTyp.ENTITY" class="entity" @dblclick="activateEntity" @click.right="activateEntity" @mousedown="mousedown($event)" />
    <IconRelationshiptyp v-if="props.entity.type == EntityTyp.RELATIONSHIP" class="entity" @dblclick="activateEntity" @click.right="activateEntity" @mousedown="mousedown($event)" />
    <IconEntityRelationshiptyp v-if="props.entity.type == EntityTyp.ENTITYRELATIONSHIP" class="entity" @dblclick="activateEntity" @click.right="activateEntity" @mousedown="mousedown($event)" />
  </div>
</template>

<script setup lang="ts">
import IconEntityRelationshiptyp from '../icons/IconEntityRelationshiptyp.vue'
import IconEntity from '../icons/IconEntitytyp.vue'
import IconRelationshiptyp from '../icons/IconRelationshiptyp.vue'
import EntityTyp from '../../enums/EntityType'
import OutgoingAnkerPoint from './OutgoingAnkerPoint.vue'
import IncomingAnkerPoint from './IncomingAnkerPoint.vue'
import EntityWidget from './EntityWidget.vue'

import { ref, onMounted, computed, watch } from 'vue'
import { useDiagramStore } from '../../stores/diagramStore'
import { useToolManagementStore } from '../../stores/toolManagementStore'

import ConnectorPosition from '../../enums/ConnectorPosition'
import type Attribute from '../../model/diagram/Attribute'
import type Entity from '../../model/diagram/Entity'

const props = defineProps<{
  entity: Entity
  isEditable: boolean
}>()

onMounted(() => {
  setAnkerPoints()
  setPosition(root.value, props.entity)
  updateAttributes()
})

// Emit new entity to parent
watch(props.entity, () => {
  setAnkerPoints()
  setPosition(root.value, props.entity)
  updateAttributes()
})

const toolManagementStore = useToolManagementStore()
const diagramStore = useDiagramStore()

const entity = computed(() => {
  return diagramStore.diagram.entities.find((entity) => entity.id == props.entity.id) ?? ({} as Entity)
})

const root = ref<HTMLInputElement | null>(null)

const isEntityHighlighted = computed(() => {
  return toolManagementStore.highlightedEntityId === props.entity.id
})

const isActive = computed(() => {
  return toolManagementStore.newConnection.startEntity == props.entity.id
})

const cssVarAttributesDistanceTop = computed(() => {
  return props.entity.width / 2 + 'px'
})

const formattedAttributes = ref('')
//const attributes = ref(props.entity.attributes)
const updateAttributes = () => {
  //clear
  formattedAttributes.value = ''

  props.entity.attributes.forEach((attribute: Attribute) => {
    if (attribute.pkey && attribute.fkey) {
      formattedAttributes.value += '<b><u><i>#*' + attribute.name + '</i></u></b>, '
    } else if (attribute.pkey && !attribute.fkey) {
      formattedAttributes.value += '<b><u>#' + attribute.name + '</u></b>, '
    } else if (!attribute.pkey && attribute.fkey) {
      formattedAttributes.value += '<i>*' + attribute.name + '</i>, '
    } else if (!attribute.pkey && !attribute.fkey) {
      formattedAttributes.value += '<span>' + attribute.name + '</span>, '
    } else {
      throw 'Not implemented Attribute Typ'
    }
  })
  // cut off ", "
  formattedAttributes.value = formattedAttributes.value.slice(0, -2)
}

const hover = ref<boolean>(false)

const isTextEditable = ref<boolean>(false)
const makeTextEditable = () => {
  if (isTextEditable.value) {
    diagramStore.saveHistory()
  }

  isTextEditable.value = !isTextEditable.value
}

const handleEnter = (e: any) => {
  if (entity.value?.entityName !== undefined) {
    const curPos = e.srcElement.selectionStart

    if (e.ctrlKey) {
      const textarea = entity.value?.entityName
      //use return to exit methode
      return (entity.value.entityName = textarea.slice(0, curPos) + '\n' + textarea.slice(curPos))
    }
    entity.value.entityName = entity.value?.entityName.slice(0, curPos - 1) + props.entity.entityName.slice(curPos)
    makeTextEditable()
  }
}

const outgoingAnkerPoint = ref<ConnectorPosition[]>([])
const incomingAnkerPoint = ref<ConnectorPosition[]>([])

const setAnkerPoints = () => {
  outgoingAnkerPoint.value = []
  incomingAnkerPoint.value = []

  switch (props.entity.type) {
    case EntityTyp.ENTITY:
      outgoingAnkerPoint.value.push(ConnectorPosition.Right)
      break
    case EntityTyp.RELATIONSHIP:
      incomingAnkerPoint.value.push(ConnectorPosition.Left)
      break
    case EntityTyp.ENTITYRELATIONSHIP:
      outgoingAnkerPoint.value.push(ConnectorPosition.Right)
      incomingAnkerPoint.value.push(ConnectorPosition.Left)
      break

    default:
      throw 'EntityTyp not defined'
  }
}

const setPosition = (element: HTMLInputElement | null, entity: Entity) => {
  if (element != null) {
    element.style.top = entity.top + 'px'
    element.style.left = entity.left + 'px'
    element.style.width = entity.width + 'px'
  }
}

const updateEntity = () => {
  if (root.value != null && entity.value != undefined) {
    entity.value.top = parseInt(root.value.style.top)
    entity.value.left = parseInt(root.value.style.left)
    entity.value.width = parseInt(root.value.style.width)
  }
}

const activateEntity = () => {
  if (!props.isEditable) {
    return
  }

  if (entity.value != undefined) {
    if (toolManagementStore.selectedEntity == entity.value) {
      toolManagementStore.selectedEntity = null
    } else toolManagementStore.selectedEntity = entity.value
  }
}

const isSelected = computed(() => {
  return toolManagementStore.selectedEntity === entity.value
})

const mousedown = (e: any) => {
  if (!props.isEditable) {
    return
  }

  //console.log(root.value.parentNode.getBoundingClientRect())

  // console.log("move");
  let el = e.target.parentNode

  if (root.value == null || root.value.parentElement == null) return
  let container = root.value.parentElement

  let prevX = e.clientX
  let prevY = e.clientY
  //console.log(`Current mouse position: X ${prevX} | Y  ${prevX}`)

  const mousemove = (e: any) => {
    let newX = prevX - e.clientX
    let newY = prevY - e.clientY
    //console.log(`NEW client position: X ${newX} | Y  ${newY}`)

    const rect = el.getBoundingClientRect()
    const rectParent = container.getBoundingClientRect()

    //calculare position relative to container
    let relativePos: { top?: number; right?: number; bottom?: number; left?: number } = {}
    relativePos.top = rect.top - rectParent.top
    relativePos.right = rect.right - rectParent.right
    relativePos.bottom = rect.bottom - rectParent.bottom
    relativePos.left = rect.left - rectParent.left

    let newLeft = relativePos.left - newX
    let newTop = relativePos.top - newY

    //console.log(`NEW Position: X ${newLeft} | Y  ${newTop}`)

    //set boundaries
    if (newLeft >= 0 && newTop >= 0 && newLeft <= rectParent.width - rect.width && newTop <= rectParent.height - rect.height) {
      el.style.left = newLeft + 'px'
      el.style.top = newTop + 'px'
    }

    //console.log(`NEW Element position: X ${el.style.left} | Y  ${el.style.top}`)

    prevX = e.clientX
    prevY = e.clientY

    updateEntity()
  }

  const mouseup = () => {
    window.removeEventListener('mousemove', mousemove)
    window.removeEventListener('mouseup', mouseup)

    //Update Entity to Send new coordinates to parent component
    updateEntity()

    //Save History
    diagramStore.saveHistory()
  }

  window.addEventListener('mousemove', mousemove)
  window.addEventListener('mouseup', mouseup)
}

const resizer = (e: any) => {
  // console.log("resize");

  let currentResizer = e.target

  let el = e.target.parentNode

  if (root.value == null || root.value.parentElement == null) return
  let container = root.value.parentElement

  let prevX = e.clientX
  let prevY = e.clientY

  const mousemove = (e: { clientX: number; clientY: number }) => {
    const rect = el.getBoundingClientRect()
    const rectParent = container.getBoundingClientRect()

    //calculare position relative to container
    let relativePos: { top?: number; right?: number; bottom?: number; left?: number } = {}
    relativePos.top = rect.top - rectParent.top
    relativePos.right = rect.right - rectParent.right
    relativePos.bottom = rect.bottom - rectParent.bottom
    relativePos.left = rect.left - rectParent.left

    // TODO: set boundaries
    // if ((newLeft>=0) && (newTop>=0) && (newLeft<=rectParent.width-rect.width) && (newTop<=rectParent.height-rect.height)) {
    //     el.style.left = newLeft + 'px'
    //     el.style.top = newTop + 'px'
    // }

    if (currentResizer.classList.contains('nw')) {
      // console.log("nw");
      el.style.width = rect.width + (prevX - e.clientX) + 'px'
      el.style.left = relativePos.left + (prevX - e.clientX) + 'px'
      el.style.top = relativePos.top - (prevY - e.clientY) + 'px'
      el.style.left = relativePos.left - (prevX - e.clientX) + 'px'
      // TODO: Bug
    } else if (currentResizer.classList.contains('ne')) {
      // console.log("ne");
      el.style.width = rect.width - (prevX - e.clientX) + 'px'
      el.style.top = relativePos.top - (prevY - e.clientY) + 'px'
      // TODO: Bug
    } else if (currentResizer.classList.contains('sw')) {
      // console.log("sw");
      el.style.width = rect.width + (prevX - e.clientX) + 'px'
      el.style.left = relativePos.left - (prevX - e.clientX) + 'px'
    } else if (currentResizer.classList.contains('se')) {
      // console.log("se");
      el.style.width = rect.width - (prevX - e.clientX) + 'px'
    }

    prevX = e.clientX
    prevY = e.clientY
  }

  const mouseup = () => {
    window.removeEventListener('mousemove', mousemove)
    window.removeEventListener('mouseup', mouseup)

    //Update Entity to Send new coordinates to parent component
    updateEntity()
  }

  window.addEventListener('mousemove', mousemove)
  window.addEventListener('mouseup', mouseup)
}
</script>

<style scoped lang="scss">
.click-area {
  position: absolute;
  width: 160%;
  height: 160%;
  top: -30%;
  left: -30%;
  z-index: -10;
  cursor: default;
}
.objectContainer {
  z-index: 5;
  position: absolute;
  cursor: move;
}

.active {
  outline: 4px solid rgb(var(--v-theme-info));
}

.resizer {
  position: absolute;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background-color: rgb(var(--v-theme-info));
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

  top: v-bind('cssVarAttributesDistanceTop');
  cursor: default;
}

.highlighted {
  border: 2px solid red;
}
</style>
