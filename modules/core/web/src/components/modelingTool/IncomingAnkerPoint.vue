<template>
  <div ref="dockingPoint" class="dockingPoint" @mousedown="createLine()"></div>
</template>

<script setup lang="ts">
import ConnectorPosition from "@/enums/ConnectorPosition";
import Entity from "@/model/diagram/Entity";
import { ref, onMounted, computed } from "vue";
import { useToolManagementStore } from "../../stores/toolManagementStore";

const toolManagementStore = useToolManagementStore();

const props = defineProps<{
  position: ConnectorPosition;
  entity: Entity;
}>();

const dockingPoint = ref<HTMLInputElement | null>(null);

onMounted(() => {
  //add position to ankerPoint
  if (dockingPoint.value != null) {
    dockingPoint.value.classList.add(props.position);
  }
});

const cssVarEntityHeight = computed(() => {
  return props.entity.width / 2 + "px";
});

const createLine = () => {
  toolManagementStore.endConnection(props.entity.id, props.position);

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
  /* TODO */
}
.dockingPoint.left {
  left: -15px;
  width: 15px;
  height: v-bind("cssVarEntityHeight");
  background-color: #e30000e8;
}
.dockingPoint.right {
  /* TODO */
}
.dockingPoint.bottom {
  /* TODO */
}
</style>
