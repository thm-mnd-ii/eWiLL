<template>
    <div
        ref="dockingPoint"
        @mousedown="createLine($event)"
        class="dockingPoint">
    </div>
    <!-- :style="{ left:  (parseInt(props.entityWidth) / 2) - 10 + 'px' }" -->
</template>

<script setup>

    import { ref, onMounted, computed, watch, reactive } from 'vue'

    const emit = defineEmits(['ankerPosition'])
    const props = defineProps(['position', 'entityWidth'])
    const dockingPoint = ref(null)


    onMounted(() => {
        //add position to ankerPoint
        dockingPoint.value.classList.add(props.position)
    })

    // berechne Entfernung nach Links um Pfeile mittig zu positionieren
    const cssVarDistanceLeft = computed(() => {
        const arrowWidth = 20
        return (parseInt(props.entityWidth) / 2) - (parseInt(arrowWidth / 2)) + 'px'
    })

    const cssVarDistanceTop = computed(() => {
        const arrowWidth = 18
        return (parseInt(props.entityWidth) / 4) - (parseInt(arrowWidth / 2)) + 'px'
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
    left: v-bind('cssVarDistanceLeft');
    top: -30px;
    border-style: solid;
    border-width: 0 10px 20px 10px;
    border-color: transparent transparent #00aae374 transparent;
}
.dockingPoint.left{
    top: v-bind('cssVarDistanceTop');
    left: -30px;
    border-style: solid;
    border-width: 10px 20px 10px 0;
    border-color: transparent #00aae374 transparent transparent;
}
.dockingPoint.right{
    top: v-bind('cssVarDistanceTop');
    right: -30px;
    border-style: solid;
    border-width: 10px 0 10px 20px;
    border-color: transparent transparent transparent #00aae374;
}
.dockingPoint.bottom{
    left: v-bind('cssVarDistanceLeft');
    bottom: -30px;
    border-style: solid;
    border-width: 20px 10px 0 10px;
    border-color: #00aae374 transparent transparent transparent;
}
</style>