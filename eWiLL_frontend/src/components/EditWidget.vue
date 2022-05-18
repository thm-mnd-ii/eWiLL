<template>
    <IconTrash @click="deleteEntity" class="widget">Edit Widget</IconTrash>
</template>

<script setup>
    import IconTrash from './icons/IconTrash.vue'

    const emit = defineEmits(['deleteEntity'])

    const deleteEntity = () => {
        emit('deleteEntity', true)
    }

</script>

<style scoped>
.widget{
    position: absolute;
    top: -35px;
    width: 30px;
    height: 30px;
    border-radius: 3px;
    padding: 3px;

    cursor: pointer;

    background-color: #fff;
}
</style>