<template>
  <div class="widgetContainer">
    <IconTrash class="widget" @click="deleteLine" />
    <IconChange class="widget" @click="changeLine" />
  </div>
</template>

<script setup>
import IconTrash from "./icons/IconTrash.vue";
import IconChange from "./icons/IconChange.vue";

const emit = defineEmits(["deleteEntity", "changeLine"]);

const deleteLine = () => {
  emit("deleteEntity", true);
};

const changeLine = () => {
  emit("changeLine", true);
};
</script>

<style scoped lang="scss">
.widgetContainer {
  position: relative;
  left: -30px;
}
.widget {
  width: 22px;
  height: 22px;
  padding: 2px;
  margin: 2px;

  border-radius: 5px;
  cursor: pointer;
  background-color: #fff;
}
</style>
