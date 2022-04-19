<template>
    <div ref="root" @mouseover="hover = true" @mouseleave="hover = false" class="objectContainer">

        <span v-if="!isEditable" @dblclick="makeTextEditable" class="text">{{props.entity.text}}</span>
        <textarea v-if="isEditable" @dblclick="makeTextEditable" @keyup.enter="handleEnter" v-model="props.entity.text" class="textedit form-control"  rows="1"></textarea>

        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer nw"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer ne"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer sw"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer se"></div>

        <AnkerPoint v-if="hover" position="top" @ankerPosition="handleAnkerPoint"></AnkerPoint>
        <AnkerPoint v-if="hover" position="left" @ankerPosition="handleAnkerPoint"></AnkerPoint>
        <AnkerPoint v-if="hover" position="right" @ankerPosition="handleAnkerPoint"></AnkerPoint>
        <AnkerPoint v-if="hover" position="bottom" @ankerPosition="handleAnkerPoint"></AnkerPoint>

        <IconEntity v-if="props.entity.typ == EntityTyp.ENTITY" @dblclick="changeResizable()" @mousedown="mousedown($event)" />
        <IconRelationshiptyp v-if="props.entity.typ == EntityTyp.RELATIONSHIP" @dblclick="changeResizable()" @mousedown="mousedown($event)" />
        <IconEntityRelationshiptyp v-if="props.entity.typ == EntityTyp.ENTITYRELATIONSHIP" @dblclick="changeResizable()" @mousedown="mousedown($event)" />
    </div>
</template>

<script setup>
    import IconEntityRelationshiptyp from "../components/icons/IconEntityRelationshiptyp.vue"
    import IconEntity from "../components/icons/IconEntitytyp.vue"
    import IconRelationshiptyp from "../components/icons/IconRelationshiptyp.vue"
    import EntityTyp from "../enums/EntityTyp"
    import AnkerPoint from "../components/AnkerPoint.vue"
    
    import { ref, onMounted, computed, watch, reactive } from 'vue'

    const emit = defineEmits(['update:entity', 'ankerPoint'])
    //const updateEntity = ref(updateCurrentEntity.value)

    const props = defineProps(['entity'])
    const root = ref(null)

    const handleAnkerPoint = (ankerPosition) => {
        // console.log(e)
        // console.log(props.entity.id)
        emit('ankerPoint', { 'id': props.entity.id, 'position': ankerPosition})
    }

    const hover = ref(false)
    watch(hover, (e) => {
        //console.log(`Hover: ${e}`)
    })

    const isEditable = ref(false)
    const makeTextEditable = () => {
        isEditable.value = !isEditable.value
    }

    const handleEnter = (e) => {
        if (e.ctrlKey) return props.entity.text += "\n"

        props.entity.text = props.entity.text.slice(0,-1)
        makeTextEditable()
    }

    // watch(isEditable, (e) => {
    //     //console.log(props.entity.text == "")
    //     if (props.entity.text == ""){
    //         props.entity.text == "Placeholder"
    //     }
    // })

    onMounted(() => {
        //console.log(props.entity)
        setPosition(root.value, props.entity)
      })
    
    const setPosition = (element, entity) => {
        element.style.top = entity.top
        element.style.left = entity.left
        element.style.width = entity.width
    }

    const updateEntity = () => {
        props.entity.top = root.value.style.top
        props.entity.left = root.value.style.left
        props.entity.width = root.value.style.width
    }

    // Emit new entity to parent
    watch(props.entity, (entity) => {
        //console.log(entity)
        emit('update:entity', entity.value)
    })

    let isResizable = ref(false)
    const changeResizable = () => {
        //console.log(isResizable.value)
        isResizable.value = !isResizable.value
    }

    const mousedown = (e) => {

        //console.log(root.value.parentNode.getBoundingClientRect())

        console.log('move')
        let el = e.target.parentNode
        let container = root.value.parentNode

        let prevX = e.clientX
        let prevY = e.clientY
        //console.log(`Current mouse position: X ${prevX} | Y  ${prevX}`)

        const mousemove = (e) => {

            let newX = prevX - e.clientX
            let newY = prevY - e.clientY
            //console.log(`NEW client position: X ${newX} | Y  ${newY}`)

            const rect = el.getBoundingClientRect()
            const rectParent = container.getBoundingClientRect()

            //calculare position relative to container
            let relativePos = {}
            relativePos.top = rect.top - rectParent.top
            relativePos.right = rect.right - rectParent.right
            relativePos.bottom = rect.bottom - rectParent.bottom
            relativePos.left = rect.left - rectParent.left

            let newLeft = (relativePos.left - newX)
            let newTop = (relativePos.top - newY)

            //console.log(`NEW Position: X ${newLeft} | Y  ${newTop}`)

            //set boundaries
            if ((newLeft>=0) && (newTop>=0) && (newLeft<=rectParent.width-rect.width) && (newTop<=rectParent.height-rect.height)) {
                el.style.left = newLeft + 'px'
                el.style.top = newTop + 'px'
            }
            

            //console.log(`NEW Element position: X ${el.style.left} | Y  ${el.style.top}`)

            prevX = e.clientX
            prevY = e.clientY
        }

        const mouseup = (e) => {
            window.removeEventListener('mousemove', mousemove)
            window.removeEventListener('mouseup', mouseup)

            //Update Entity to Send new coordinates to parent component
            updateEntity()
        }

        window.addEventListener('mousemove', mousemove)
        window.addEventListener('mouseup', mouseup)
          
    }

    const resizer = (e) => {

        console.log('resize')

        let currentResizer = e.target

        let el = e.target.parentNode 
        let container = root.value.parentNode

        let prevX = e.clientX
        let prevY = e.clientY

        const mousemove = (e) => {

            const rect = el.getBoundingClientRect()
            const rectParent = container.getBoundingClientRect()

            //calculare position relative to container
            let relativePos = {}
            relativePos.top = rect.top - rectParent.top
            relativePos.right = rect.right - rectParent.right
            relativePos.bottom = rect.bottom - rectParent.bottom
            relativePos.left = rect.left - rectParent.left

            // TODO: set boundaries
            // if ((newLeft>=0) && (newTop>=0) && (newLeft<=rectParent.width-rect.width) && (newTop<=rectParent.height-rect.height)) {
            //     el.style.left = newLeft + 'px'
            //     el.style.top = newTop + 'px'
            // }

            if(currentResizer.classList.contains('nw')){
                console.log('nw')
                el.style.width = rect.width + (prevX - e.clientX) + 'px'
                el.style.left = relativePos.left + (prevX - e.clientX) + 'px'
                el.style.top = relativePos.top - (prevY - e.clientY) + 'px'
                el.style.left = relativePos.left - (prevX - e.clientX) + 'px'
                // TODO: Bug 
            }
            else if(currentResizer.classList.contains('ne')){
                console.log('ne')
                el.style.width = rect.width - (prevX - e.clientX) + 'px'
                el.style.top = relativePos.top - (prevY - e.clientY) + 'px'
                // TODO: Bug
            }
            else if(currentResizer.classList.contains('sw')){
                console.log('sw')
                el.style.width = rect.width + (prevX - e.clientX) + 'px'
                el.style.left = relativePos.left - (prevX - e.clientX) + 'px'
            }
            else if(currentResizer.classList.contains('se')){
                console.log('se')
                el.style.width = rect.width - (prevX - e.clientX) + 'px'
            }

            prevX = e.clientX
            prevY = e.clientY
        }

        const mouseup = (e) => {
            window.removeEventListener('mousemove', mousemove)
            window.removeEventListener('mouseup', mouseup)

            //Update Entity to Send new coordinates to parent component
            updateEntity()
        }

        window.addEventListener('mousemove', mousemove)
        window.addEventListener('mouseup', mouseup)
    }

</script>

<style scoped>
.objectContainer {
    z-index: 5;
    position: absolute;
    cursor: move;
}

.resizer {
    position: absolute;
    width: 8px;
    height: 8px;
    border-radius: 4px;
    background-color: #00abe3;
    z-index: 10;
}

.resizer.nw {
    top: -2px;
    left: -2px;
    cursor: nw-resize;
}

.resizer.ne {
    top: -2px;
    right: -2px;
    cursor: ne-resize;
}

.resizer.sw {
    bottom: -2px;
    left: -2px;
    cursor: sw-resize;
}

.resizer.se {
    bottom: -2px;
    right: -2px;
    cursor: se-resize;
}

.text{
    min-width: 10px;
    min-height: 10px;

    text-align: center;
    display: inline-block;
    font-size: smaller;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    cursor: text;
}

.textedit{
    text-align: center;
    resize: none;
    overflow: hidden;
    width: 95%;
    height: 90%;
    position: absolute;
    font-size: smaller;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

</style>