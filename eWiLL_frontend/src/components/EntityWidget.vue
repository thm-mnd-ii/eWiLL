<template>
  <div class="widgetContainer">
    <IconTrash class="widget" @click="deleteEntity">Edit Widget</IconTrash>
    <IconChange class="widget" @click="changeEntityTyp" />
    <IconAdd class="widget" @click="manageAttributes" />
  </div>
</template>

<script setup>
import IconTrash from "./icons/IconTrash.vue";
import IconChange from "./icons/IconChange.vue";
import IconAdd from "./icons/IconAdd.vue";

const emit = defineEmits(["delete-entity", "change-entity-typ", "manage-attributes"]);

const deleteEntity = () => {
  emit("delete-entity", true);
};

const changeEntityTyp = () => {
  emit("change-entity-typ", true);
};

const manageAttributes = () => {
  emit("manage-attributes", true);
};
</script>

<style scoped lang="scss">
.widgetContainer {
  position: absolute;
  top: -35px;
}
.widget {
  width: 25px;
  height: 25px;
  padding: 2px;
  margin: 2px;

  border-radius: 5px;
  cursor: pointer;
  background-color: rgb(255, 255, 255);
}
</style>
