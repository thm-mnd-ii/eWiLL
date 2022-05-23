<template>

    <div>
        <div class="widgetContainer">
            <IconTrash @click="deleteLine" class="widget">Edit Widget</IconTrash>
        </div>
    </div>
    
</template>

<script setup>
    import IconTrash from './icons/IconTrash.vue'
    import { ref } from 'vue'

    const emit = defineEmits(['deleteEntity'])

    const deleteLine = () => {
        emit('deleteEntity', true)
    }

</script>

<style scoped>
.widget{
    width: 22px;
    height: 22px;
    padding: 2px;

    border-radius: 5px;
    cursor: pointer;
    background-color: #fff;
}

</style>