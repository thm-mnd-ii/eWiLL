<template>
    <LineWidget
        v-if="visible"
        @deleteEntity="deleteLine"
        class="lineWidget"
        :style="{ top: (props.line.y1 + props.line.y2) / 2 + 'px', left: (props.line.x1 + props.line.x2) / 2 + 'px' }"
    />

    <!-- <span
        v-if="hover"
        class="dot"
        @click="changeVisibility"
        :style="{ top: (props.line.y1 + props.line.y2) / 2 - 4 + 'px', left: (props.line.x1 + props.line.x2) / 2 - 4 + 'px' }"
    /> -->

    <!-- Hide SVG and show only line, to make line clickable -->
    <svg ref="root" class="svgContainer hide">
        <line class="reshow" @dblclick="changeVisibility" stroke-width="2.5px" stroke="#000000"  x1="0" y1="0" x2="0" y2="0" marker-end="url(#arrowhead)" id="svgLine"/>
    </svg>
</template>

<script setup>

    import LineWidget from "../components/LineWidget.vue"
    import { ref, onMounted, computed, watch, reactive } from 'vue'
    const emit = defineEmits(['deleteLine'])

    const props = defineProps(['line'])
    const root = ref(null)

    let visible = ref(false)
    const changeVisibility = () => {
        visible.value = !visible.value
    }

    const hover = ref(false)
    watch(hover, (e) => {
        //console.log(`Hover: ${e}`)
    })

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

    const deleteLine = () => {
        changeVisibility()
        emit('deleteLine', props.line)
    }

</script>

<style scoped>


.svgContainer {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 15;
}

.svgContainer > line{
    width: 150%;
    height: 150%;
}

.svgContainer > line:hover{
    stroke: #00abe3;
}

.lineWidget {
    position: absolute;
    z-index: 5;
}

/* Hide Parent to reshow only svgLine */
.hide {visibility: hidden;}
.reshow {visibility: visible;}

.dot {
    position: absolute;
    height: 8px;
    width: 8px;

    z-index: 10;
    background-color: #00abe3;
    border-radius: 50%;
    cursor: pointer;
}

</style>