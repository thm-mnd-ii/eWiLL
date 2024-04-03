<template>
  <svg v-if="finalSVG" :width="computedWidth" :height="computedHeight" version="1.1" xmlns="http://www.w3.org/2000/svg" view-box="0 0 100 100">
    <path v-for="shape in finalSVG?.filter((shape) => shape.type === 'path')" :key="shape.attributes.id" :d="shape.attributes.d" fill="none" stroke="#000" stroke-width="1" />

    <path d="M 0 0 L 100 0 L 100 100 L 0 100 Z" fill="none" stroke="#000" stroke-width="1" />
  </svg>
</template>

<script setup lang="ts">
import { computed, defineProps, onMounted, watch, ref } from 'vue'
import type { PropType } from 'vue'
import { SVG } from '@svgdotjs/svg.js'

const finalSVG = ref<SvgShape[]>()

onMounted(() => {})

interface SvgShape {
  type: string
  attributes: Record<string, string>
}

const normalizeAndCenterPathD = (d: string) => {
  const path = SVG().path(d)
  const box = path.bbox()

  console.log(box)

  const x = box.x
  const y = box.y
  const width = box.width
  const height = box.height

  const offsetX = -x + width / 2
  const offsetY = -y + height / 2

  path.move(offsetX, offsetY)

  console.log(path)
}

const extractShapes = (svgElement: SVGElement): SvgShape[] => {
  const shapes: SvgShape[] = []
  const elementsToExtract = ['path', 'circle', 'rect'] // Fügen Sie weitere Tags hinzu, die Sie extrahieren möchten

  elementsToExtract.forEach((tagName) => {
    svgElement.querySelectorAll(tagName).forEach((element) => {
      const attributes: Record<string, string> = {}
      for (const attr of element.attributes) {
        attributes[attr.name] = attr.value
      }

      if (tagName === 'path') {
        normalizeAndCenterPathD(attributes.d)
      }

      shapes.push({
        type: tagName,
        attributes
      })
    })
  })

  console.log(shapes)
  finalSVG.value = shapes

  return shapes
}

const props = defineProps({
  svgObject: {
    type: SVGElement as PropType<SVGElement>,
    required: true
  },
  width: {
    type: String as PropType<string>,
    default: '100%'
  },
  height: {
    type: String as PropType<string>,
    default: '100%'
  }
})

const computedWidth = computed(() => {
  return props.width
})

const computedHeight = computed(() => {
  return props.height
})

watch(
  () => props.svgObject,
  (newValue) => {
    console.log(newValue)
    extractShapes(newValue)
  }
)
</script>

<style lang="scss" scoped></style>
