<template>
  <div class="widgetContainer">
    <span>
      <v-icon class="widget" icon="mdi-swap-horizontal" color="info" @click="changeLineStyle"></v-icon>
      <v-tooltip activator="parent" location="top">Change Type</v-tooltip>
    </span>
    <span>
      <v-icon class="widget" icon="mdi-delete-outline" color="error" @click="deleteLine"></v-icon>
      <v-tooltip activator="parent" location="top">Delete</v-tooltip>
    </span>
  </div>
</template>

<script setup lang="ts">
import { useDiagramStore } from "../../stores/diagramStore";
import CardinalityType from "../../enums/CardinalityType";

const diagramStore = useDiagramStore();

const props = defineProps({
  line: { type: Object, required: true },
});

const deleteLine = () => {
  diagramStore.diagram.connections.splice(props.line.id, 1);
  diagramStore.saveHistory();
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
  diagramStore.saveHistory();
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
