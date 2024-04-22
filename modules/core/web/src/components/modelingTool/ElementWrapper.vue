<template>
  <div v-html="finalSVG?.outerHTML"></div>
</template>

<script setup lang="ts">
import { defineProps, onMounted, watch, ref } from 'vue'
import type { PropType } from 'vue'

const finalSVG = ref<SVGElement>()

onMounted(() => {})

const svgPipeline = async (svgElement: SVGElement) => {
  console.log('start pipeline', svgElement)

  svgElement.setAttribute('height', props.height)
  svgElement.removeAttribute('width')

  finalSVG.value = svgElement
}

const props = defineProps({
  svgObject: {
    type: SVGElement as PropType<SVGElement>,
    required: true
  },
  height: {
    type: String as PropType<string>,
    default: '100'
  }
})

watch(
  () => props.svgObject,
  (newValue) => {
    svgPipeline(newValue)
  }
)
</script>

<style lang="scss" scoped></style>
