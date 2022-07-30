<template>
    <div ref="root" class="objectContainer" @mouseover="hover = true" @mouseleave="endHover">

        <span v-if="!isEditable" class="text unselectable" @dblclick="makeTextEditable">{{props.entity.entityName}}</span>
        <textarea v-if="isEditable" v-model="props.entity.entityName" class="textedit form-control"  rows="1" @dblclick="makeTextEditable" @keyup.enter="handleEnter"></textarea>

        <span class="attributes" v-html="formattedAttributes"></span>

        <!-- <div v-if="isResizable" @mousedown="resizer($event)" class="resizer nw"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer ne"></div>
        <div v-if="isResizable" @mousedown="resizer($event)" class="resizer sw"></div> -->
        <div v-if="isResizable" class="resizer se" @mousedown="resizer($event)"></div>

        <!-- <AnkerPoint v-if="hover && !isResizable" position="top" :entityWidth="props.entity.width" @ankerPosition="handleAnkerPoint"></AnkerPoint> -->
        <AnkerPoint v-if="hover && !isResizable" position="left" :entity-width="props.entity.width" @anker-position="handleAnkerPoint"></AnkerPoint>
        <AnkerPoint v-if="hover && !isResizable" position="right" :entity-width="props.entity.width" @anker-position="handleAnkerPoint"></AnkerPoint>
        <!-- <AnkerPoint v-if="hover && !isResizable" position="bottom" :entityWidth="props.entity.width" @ankerPosition="handleAnkerPoint"></AnkerPoint> -->

        <EntityWidget v-if="isResizable" @delete-entity="deleteEntity" @change-entity-typ="changeEntityTyp" @manage-attributes="manageAttributes" />

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
    import AttributeTyp from "../enums/AttributeTyp"
    import AnkerPoint from "../components/AnkerPoint.vue"
    import EntityWidget from "../components/EntityWidget.vue"
    
    import { ref, onMounted, computed, watch } from 'vue'

    const emit = defineEmits(['update:entity', 'anker.point', 'delete-entity', 'change-entity-typ', 'manage-attributes'])
    //const updateEntity = ref(updateCurrentEntity.value)

    const props = defineProps({
        entity: { type: Object, required: true}
    })

    const root = ref(null)

    const cssVarAttributesDistanceTop = computed(() => {
        return (parseInt(props.entity.width) / 2) + 'px'
    })

    const formattedAttributes = ref("")
    //const attributes = ref(props.entity.attributes)
    const updateAttributes = () => {
        //clear
        formattedAttributes.value = ""

        props.entity.attributes.forEach(attribute => {

            switch (attribute.typ) {
                case AttributeTyp.PK:
                    formattedAttributes.value += "<b>" + attribute.name + "</b>, "
                    break;

                case AttributeTyp.FK:
                    formattedAttributes.value += "<u>" + attribute.name + "</u>, "
                    break;

                case AttributeTyp.ATTRIBUT:
                    formattedAttributes.value += "<i>" + attribute.name + "</i>, "
                    break;
            
                default:
                    throw "Not implemented Attribute Typ"
            }
        })
        
        // cut off ", "
        formattedAttributes.value = formattedAttributes.value.slice(0, -2)
    }

    const deleteEntity = () => {
        emit('delete-entity', props.entity)
    }

    const changeEntityTyp = () => {
        emit('change-entity-typ', props.entity)
    }

    const manageAttributes = () => {
        emit('manage-attributes', props.entity)
    }

    const handleAnkerPoint = (ankerPosition) => {
        // console.log(e)
        // console.log(props.entity.id)
        emit('anker-point', { 'id': props.entity.id, 'position': ankerPosition})
    }

    const hover = ref(false)
    watch(hover, (e) => {
        //console.log(`Hover: ${e}`)
    })

    const endHover = () => {
        setTimeout(() => { hover.value = false }, 1000)
    }

    const isEditable = ref(false)
    const makeTextEditable = () => {
        isEditable.value = !isEditable.value
    }

    const handleEnter = (e) => {
        if (e.ctrlKey){
            const curPos = e.srcElement.selectionStart
            const textarea = props.entity.text
            //use return to exit methode
            return props.entity.text = textarea.slice(0, curPos) + "\n" + textarea.slice(curPos)
        }

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
        updateAttributes()
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
        updateAttributes()
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

.unselectable {
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

.attributes {
    font-size: x-small;
    text-align: left;
    vertical-align: text-top;

    position: absolute;

    top: v-bind('cssVarAttributesDistanceTop');
    cursor: text;
}

</style>