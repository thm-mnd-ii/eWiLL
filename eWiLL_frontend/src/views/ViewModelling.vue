<template>

    <div>
        {{entityList}}
    </div>

    <div class="toolbox">
        <IconEntity id="item" draggable="true" @click="addElement($event, EntityTyp.ENTITY)"/>
        <IconRelationshiptyp @mousedown="addElement($event, EntityTyp.RELATIONSHIP)"/>
        <IconEntityRelationshiptyp @mousedown="addElement($event, EntityTyp.ENTITYRELATIONSHIP)"/>
    </div>

    <div class="modellingContainer">
        <Entity v-for="entity in entityList" :key="entity.id" :entity="entity" />

        <Line v-for="line in lineList" :key="line.id" :line="line" />
    </div>
    
</template>

<script setup>
    import Entity from "../components/Entity.vue"
    import Line from "../components/Line.vue"

    import IconEntityRelationshiptyp from "../components/icons/IconEntityRelationshiptyp.vue"
    import IconEntity from "../components/icons/IconEntitytyp.vue"
    import IconRelationshiptyp from "../components/icons/IconRelationshiptyp.vue"

    import EntityTyp from "../enums/EntityTyp"

    import { ref } from 'vue'

    const count = ref(0)

    const entityList = ref([
        { "id": 1, "typ": 1, "top": "100px", "left": "200px", "width": "100px" },
        // { "id": 2, "typ": 2, "top": "200px", "left": "200px", "width": "100px" },
        // { "id": 3, "typ": 3, "top": "300px", "left": "200px", "width": "100px" }
    ])

    const lineList = ref([
        { "id": 1, "x1": 200, "y1": 100, "x2": 0, "y2": 0},
        { "id": 2, "x1": 0, "y1": 0, "x2": 0, "y2": 0},
        // { "id": 3}
    ])

    const addElement = (e, typ) => {
        
        console.log(e.clientX)
        console.log(e.clientY)

        entityList.value.push({ "id": 4, "typ": typ, "text": "Entity", "top": e.clientY-25+"px", "left": e.clientX-50+"px", "width": "100px" })
    }
    
</script>

<style scoped>
.modellingContainer{
    position: absolute;
    background-color: lavender;
    left: 15%;
    top: 10%;
    width: 70%;
    height: 80%;
    padding-bottom: 10%;
}

.toolbox{
    z-index: 1;
    position: fixed;
    top: 100px;
    left: 30px;
    padding: 10px;
    width: 140px;
    height: auto;
    border-radius: 5px;
    box-shadow: 4px 4px 10px 0px rgba(15, 33, 47, 0.421);
}

.toolbox>*{
    margin: 10px;
    cursor: copy;
}

</style>