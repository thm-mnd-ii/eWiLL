<template>
    <svg ref="root" class="line">
        <line stroke-width="2px" stroke="#000000"  x1="0" y1="0" x2="0" y2="0" id="svgLine"/>
    </svg>
</template>

<script setup>

    import { ref, onMounted, computed, watch, reactive } from 'vue'

    const props = defineProps(['line'])
    const root = ref(null)

    onMounted(() => {
        //console.log(root.value.children[0].id)
        updatePosition()
      })
    
    const updatePosition = () => {
        let element = root.value.children[0]
        let entity = props.line

        element.id = entity.id
        element.x1.baseVal.value = entity.x1
        element.y1.baseVal.value = entity.y1
        element.x2.baseVal.value = entity.x2
        element.y2.baseVal.value = entity.y2
    }

    watch(props, () => {
        updatePosition()
    })

</script>

<style scoped>


.line {
    position: absolute;
    width: 100%;
    height: 100%;
}

</style>