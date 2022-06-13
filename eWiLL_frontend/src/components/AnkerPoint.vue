<template>
    <div
        ref="dockingPoint"
        @mousedown="createLine($event)"
        class="dockingPoint"
        :style="{ left:  (parseInt(props.entityWidth) / 2) - 10 + 'px' }"
    ></div>
</template>

<script setup>

    import { ref, onMounted, computed, watch, reactive } from 'vue'

    const emit = defineEmits(['ankerPosition'])
    const props = defineProps(['position', 'entityWidth'])
    const dockingPoint = ref(null)

    onMounted(() => {
        //add position to ankerPoint
        dockingPoint.value.classList.add(props.position)
        console.log(props.entityWidth)
    })

    const createLine = (e) => {

        emit('ankerPosition', props.position)
            
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
    }

</script>

<style scoped>
.dockingPoint {
    position: absolute;
    /* width: 8px;
    height: 8px;
    border-radius: 4px;
    background-color: #00abe3; */
    z-index: 10;

    cursor: pointer;
}

.dockingPoint.top{
    top: -20px;
    border-style: solid;
    border-width: 0 10px 20px 10px;
    border-color: transparent transparent #00aae3a9 transparent;
}
.dockingPoint.left{
    top: 50%;
    left: -2px;
}
.dockingPoint.right{
    top: 50%;
    right: -2px;
}
.dockingPoint.bottom{
    bottom: -2px;
    left: 50%;
}
</style>