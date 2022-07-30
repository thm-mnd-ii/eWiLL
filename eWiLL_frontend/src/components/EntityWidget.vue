<template>
    <div class="widgetContainer">
        <IconTrash @click="deleteEntity" class="widget">Edit Widget</IconTrash>
        <IconChange @click="changeEntityTyp" class="widget" />
        <IconAdd @click="manageAttributes" class="widget" />
    </div>
</template>

<script setup>
    import IconTrash from './icons/IconTrash.vue'
    import IconChange from './icons/IconChange.vue'
    import IconAdd from './icons/IconAdd.vue'

    const emit = defineEmits(['deleteEntity', 'changeEntityTyp', 'manageAttributes'])

    const deleteEntity = () => {
        emit('deleteEntity', true)
    }

    const changeEntityTyp = () => {
        emit('changeEntityTyp', true)
    }

    const manageAttributes = () => {
        emit('manageAttributes', true)
    }

</script>

<style scoped>

.widgetContainer{
    position: absolute;
    top: -35px;
}
.widget{
    width: 25px;
    height: 25px;
    padding: 2px;
    margin: 2px;

    border-radius: 5px;
    cursor: pointer;
    background-color: rgb(255, 255, 255);
}

</style>