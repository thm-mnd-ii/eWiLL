<template>
  <div ref="dockingPoint" class="dockingPoint" @mousedown="createLine()"></div>
</template>

<script setup lang="ts">
import Entity from "@/model/diagram/Entity";
import { ref, onMounted, computed } from "vue";
import ConnectorPosition from "../../enums/ConnectorPosition";
import { useToolManagementStore } from "../../stores/toolManagementStore";

const toolManagementStore = useToolManagementStore();

const dockingPoint = ref<HTMLInputElement | null>(null);

const props = defineProps<{
  position: ConnectorPosition;
  entity: Entity;
}>();

onMounted(() => {
  //add position to ankerPoint
  if (dockingPoint.value != null) {
    dockingPoint.value.classList.add(props.position);
  }
});

// berechne Entfernung nach Links um Pfeile mittig zu positionieren
const cssVarDistanceLeft = computed(() => {
  const arrowWidth = 20;
  return props.entity.width / 2 - arrowWidth / 2 + "px";
});

const cssVarDistanceTop = computed(() => {
  const arrowWidth = 18;
  return props.entity.width / 4 - arrowWidth / 2 + "px";
});

const createLine = () => {
  toolManagementStore.startConnection(props.entity.id, props.position);
  // emit("anker-position", props.position);

  //ankerPoint-Line Handler
  //watch ref() NewAnkerPoint
  //if trigger once makr as start point
  //if triggered twice mark as end point
  //watch ref() if NewAnkerPont is complete add to ankerPoints and draw line

  // let el = e.target
  // let container = el.parentNode.parentNode

  // const rect = el.getBoundingClientRect()
  // const rectParent = container.getBoundingClientRect()

  // //calculare position relative to container
  // let relativePos = {}
  // relativePos.top = rect.top - rectParent.top
  // relativePos.right = rect.right - rectParent.right
  // relativePos.bottom = rect.bottom - rectParent.bottom
  // relativePos.left = rect.left - rectParent.left

  // // TODO set this position as anker (ankerpunkt)
  // console.log(relativePos)
};
</script>

<style scoped lang="scss">
.dockingPoint {
  position: absolute;
  /* width: 8px;
    height: 8px;
    border-radius: 4px;
    background-color: #00abe3; */
  z-index: 10;

  cursor: pointer;
}

.dockingPoint.top {
  left: v-bind("cssVarDistanceLeft");
  top: -30px;
  border-style: solid;
  border-width: 0 10px 20px 10px;
  border-color: transparent transparent rgb(var(--v-theme-info), 0.6) transparent;
}
.dockingPoint.left {
  top: v-bind("cssVarDistanceTop");
  left: -30px;
  border-style: solid;
  border-width: 10px 20px 10px 0;
  border-color: transparent rgb(var(--v-theme-info), 0.6) transparent transparent;
}
.dockingPoint.right {
  top: v-bind("cssVarDistanceTop");
  right: -30px;
  border-style: solid;
  border-width: 10px 0 10px 20px;
  border-color: transparent transparent transparent rgb(var(--v-theme-info), 0.6);
}
.dockingPoint.bottom {
  left: v-bind("cssVarDistanceLeft");
  bottom: -30px;
  border-style: solid;
  border-width: 20px 10px 0 10px;
  border-color: rgb(var(--v-theme-info), 0.6) transparent transparent transparent;
}
</style>
