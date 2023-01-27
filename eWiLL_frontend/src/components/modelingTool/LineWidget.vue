<template>
  <div class="widgetContainer">
    <IconTrash class="widget" @click="deleteLine" />
    <IconChange class="widget" @click="changeLineStyle" />
  </div>
</template>

<script setup lang="ts">
import { useDiagramStore } from "../../stores/diagramStore";

import IconTrash from "../icons/IconTrash.vue";
import IconChange from "../icons/IconChange.vue";
import CardinalityType from "../../enums/CardinalityType";

const diagramStore = useDiagramStore();

const props = defineProps({
  line: { type: Object, required: true },
});

const deleteLine = () => {
  diagramStore.diagram.connections.splice(props.line.id, 1);
};

const changeLineStyle = () => {
  const currentStyle = diagramStore.diagram.connections[props.line.id].style;
  if (currentStyle != undefined) {
    if (currentStyle == CardinalityType.ONE_TO_ONE) {
      diagramStore.diagram.connections[props.line.id].style = 0;
    } else {
      diagramStore.diagram.connections[props.line.id].style = currentStyle + 1;
    }
  }
};
</script>

<style scoped lang="scss">
.widgetContainer {
  position: relative;
  left: -30px;
}
.widget {
  width: 22px;
  height: 22px;
  padding: 2px;
  margin: 2px;

  border-radius: 5px;
  cursor: pointer;
  background-color: #fff;
}
</style>
