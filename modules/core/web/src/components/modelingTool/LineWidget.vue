<template>
  <div class="widgetContainer">
    <span>
      <v-icon class="widget" icon="mdi-chevron-left" color="info" @click="previousLineStyle"></v-icon>
      <v-tooltip activator="parent" location="top">Vorheriger Typ</v-tooltip>
    </span>
    <span>
      <v-icon class="widget" icon="mdi-chevron-right" color="info" @click="nextLineStyle"></v-icon>
      <v-tooltip activator="parent" location="top">Nächster Typ</v-tooltip>
    </span>

    <span v-if="showLineStyles">
      <v-btn class="widget" color="info" variant="text" @click="setLineStyle(CardinalityType.ZERO_TO_ONE)">0:1</v-btn>
    </span>
    <span v-if="showLineStyles">
      <v-btn class="widget" color="info" variant="text" @click="setLineStyle(CardinalityType.ONE_TO_ONE)">1:1</v-btn>
    </span>
    <span v-if="showLineStyles">
      <v-btn class="widget" color="info" variant="text" @click="setLineStyle(CardinalityType.ZERO_TO_MANY)">0:*</v-btn>
    </span>
    <span v-if="showLineStyles">
      <v-btn class="widget" color="info" variant="text" @click="setLineStyle(CardinalityType.ONE_TO_MANY)">1:*</v-btn>
    </span>

    <span>
      <v-icon v-if="!showLineStyles" class="widget" icon="mdi-arrow-collapse-down" size="x-small" color="info" @click="showLineStyles = true"></v-icon>
      <v-tooltip v-if="!showLineStyles" activator="parent" location="top">Wähle aus Liste</v-tooltip>
      <v-icon v-if="showLineStyles" class="widget" icon="mdi-arrow-expand-up" size="x-small" color="info" @click="showLineStyles = false"></v-icon>
      <v-tooltip v-if="showLineStyles" activator="parent" location="top">Schließen</v-tooltip>
    </span>

    <span>
      <v-icon class="widget" icon="mdi-delete-outline" color="error" @click="deleteLine"></v-icon>
      <v-tooltip activator="parent" location="top">Löschen</v-tooltip>
    </span>
  </div>
</template>

<script setup lang="ts">
import { useDiagramStore } from "../../stores/diagramStore";
import { useToolManagementStore } from "../../stores/toolManagementStore";
import CardinalityType from "../../enums/CardinalityType";
import { ref } from "vue";

const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const props = defineProps({
  line: { type: Object, required: true },
});

const showLineStyles = ref(false);

const deleteLine = () => {
  diagramStore.diagram.connections.splice(props.line.id, 1);
  diagramStore.saveHistory();
};

const nextLineStyle = () => {
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

const previousLineStyle = () => {
  const currentStyle = diagramStore.diagram.connections[props.line.id].style;
  if (currentStyle != undefined) {
    if (currentStyle == CardinalityType.ZERO_TO_ONE) {
      diagramStore.diagram.connections[props.line.id].style = 2;
    } else {
      diagramStore.diagram.connections[props.line.id].style = currentStyle - 1;
    }
  }
  diagramStore.saveHistory();
};

const setLineStyle = (style: CardinalityType) => {
  diagramStore.diagram.connections[props.line.id].style = style;
  diagramStore.saveHistory();
};
</script>

<style scoped lang="scss">
.widgetContainer {
  position: relative;
  left: -30px;

  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}
.widget {
  width: 30px;
  height: 22px;
  padding: 2px;
  margin: 2px;

  min-width: 0;

  border-radius: 5px;
  cursor: pointer;
  background-color: #fff;
}
</style>
