<template>
  <LineWidget v-if="visible" :line="props.line" class="lineWidget" :style="{ top: (props.line.y1 + props.line.y2) / 2 + 'px', left: (props.line.x1 + props.line.x2) / 2 + 'px' }" />

  <!-- <span
        v-if="hover"
        class="dot"
        @click="changeVisibility"
        :style="{ top: (props.line.y1 + props.line.y2) / 2 - 4 + 'px', left: (props.line.x1 + props.line.x2) / 2 - 4 + 'px' }"
    /> -->

  <svg ref="root" class="svgContainer hide">
    <line id="svgLine" class="reshow" stroke-width="2.5px" stroke="#000000" x1="0" y1="0" x2="0" y2="0" @click="activateLine" />
    <line id="svgSupportLine" class="reshow" fill="none" stroke-width="2.6px" stroke="none" stroke-opacity="0" x1="0" y1="0" marker-end="url(#arrowhead)" y2="0" x2="0" @click="activateLine" />
  </svg>

  <!-- Hide SVG and show only line, to make line clickable -->
  <!-- Do not use this... -->

  <!-- Kante 0:1 -->
  <!-- <svg v-if="lineStyle == 0" ref="root" class="svgContainer hide">
        <line class="reshow" @dblclick="changeVisibility" stroke-width="2.5px" stroke="#000000"  x1="0" y1="0" x2="0" y2="0" id="svgLine"/>
    </svg> -->

  <!-- Kante 0:* -->
  <!-- <svg v-if="lineStyle == 1" ref="root" class="svgContainer hide">
        <line class="reshow" @dblclick="changeVisibility" stroke-width="2.5px" stroke="#000000"  x1="0" y1="0" x2="0" y2="0" marker-end="url(#arrowhead)" id="svgLine"/>
    </svg> -->

  <!-- Kante 1:* -->
  <!-- <svg v-if="lineStyle == 2" ref="root" class="svgContainer hide">
        <line class="reshow" @dblclick="changeVisibility" fill="none" stroke-width="2px" stroke="#000000" filter="url(#double)" x1="0" y1="0" x2="0" y2="0" id="svgLine"/>
        <line class="reshow" @dblclick="changeVisibility" fill="none" stroke-width="2.6px" stroke="none" stroke-opacity="0"  x1="0" y1="0" x2="0" y2="0" marker-end="url(#arrowhead)" id="svgLine"/>
    </svg> -->

  <!-- Kante 1:1 -->
  <!-- <svg v-if="lineStyle == 3" ref="root" class="svgContainer hide">
        <line class="reshow" @dblclick="changeVisibility" fill="none" stroke-width="2px" stroke="#000000" filter="url(#double)" x1="0" y1="0" x2="0" y2="0" id="svgLine"/>
    </svg> -->
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from "vue";
import { useToolManagementStore } from "../../stores/toolManagementStore";

import LineWidget from "./LineWidget.vue";

import CardinalityTyp from "../../enums/CardinalityType";

const props = defineProps({
  line: { type: Object, required: true },
});

const root = ref<SVGElement | null>(null);

const toolManagementStore = useToolManagementStore();

const activateLine = () => {
  if (toolManagementStore.selectedLine == props.line) {
    toolManagementStore.selectedLine = null;
  } else {
    toolManagementStore.selectedLine = props.line;
  }
};

let visible = computed(() => {
  return toolManagementStore.selectedLine == props.line;
});

//const hover = ref(false)
// watch(hover, (e) => {
//     console.log(`Hover: ${e}`)
// })

onMounted(() => {
  updateLine();
});

const updateLine = () => {
  if (root.value == null) return;

  let mainLine: any = root.value.children[0];
  let supportLine: any = root.value.children[1];

  let entity = props.line;

  mainLine.id = entity.id;
  mainLine.x1.baseVal.value = entity.x1;
  mainLine.y1.baseVal.value = entity.y1;
  mainLine.x2.baseVal.value = entity.x2;
  mainLine.y2.baseVal.value = entity.y2;

  supportLine.x1.baseVal.value = entity.x1;
  supportLine.y1.baseVal.value = entity.y1;
  supportLine.x2.baseVal.value = entity.x2;
  supportLine.y2.baseVal.value = entity.y2;

  switch (entity.style) {
    case CardinalityTyp.ZERO_TO_ONE:
      mainLine.style.markerEnd = "";
      mainLine.style.filter = "";
      mainLine.style.strokeWidth = "2.5";
      supportLine.style.display = "none";
      break;
    case CardinalityTyp.ZERO_TO_MANY:
      mainLine.style.markerEnd = "url(#arrowhead)";
      mainLine.style.filter = "";
      mainLine.style.strokeWidth = "2.5";
      supportLine.style.display = "none";
      break;
    case CardinalityTyp.ONE_TO_MANY:
      mainLine.style.markerEnd = "";
      mainLine.style.filter = "url(#double)";
      mainLine.style.strokeWidth = "2.0";
      supportLine.style.display = "block";
      break;
    case CardinalityTyp.ONE_TO_ONE:
      mainLine.style.markerEnd = "";
      mainLine.style.filter = "url(#double)";
      mainLine.style.strokeWidth = "2.0";
      supportLine.style.display = "none";
      break;

    default:
      throw "Line style is not valid.";
  }
};

watch(props, () => {
  updateLine();
});

// const changeLineStyle = () => {
//   emit("change-line", props.line);
// };

// const deleteLine = () => {
//   changeVisibility();
//   emit("delete-line", props.line);
// };
</script>

<style scoped lang="scss">
.svgContainer {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 2;
}

.svgContainer > line {
  width: 150%;
  height: 150%;
}

.svgContainer > line:hover {
  stroke: #00abe3;
}

.lineWidget {
  position: absolute;
  z-index: 2;
}

/* Hide Parent to reshow only svgLine */
.hide {
  visibility: hidden;
}

.reshow {
  visibility: visible;
}

/* .dot {
    position: absolute;
    height: 8px;
    width: 8px;

    z-index: 10;
    background-color: #00abe3;
    border-radius: 50%;
    cursor: pointer;
} */
</style>
