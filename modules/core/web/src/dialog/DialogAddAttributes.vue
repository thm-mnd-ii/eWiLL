<template>
  <v-dialog v-if="toolManagementStore.selectedEntity != undefined" v-model="dialog" class="container">
    <v-card>
      <v-card-title> Attribute </v-card-title>
      <v-card-subtitle>
        Entität: <b>{{ toolManagementStore.selectedEntity.entityName }}</b>
      </v-card-subtitle>

      <v-card-text class="card-text">
        <v-form class="imput-form" @submit.prevent>
          <v-text-field v-model="newAttributeName" label="Attribut Name" variant="solo" required :hide-details="true" @keydown.enter="addAttribute" />
          <v-btn class="btn" color="success" @click="addAttribute">Hinzufügen</v-btn>
        </v-form>

        <draggable v-model="toolManagementStore.selectedEntity.attributes" class="draggable" ghost-class="ghost" group="people" item-key="id" @start="drag = true" @end="drag = false">
          <template #item="{ element }">
            <v-card :elevation="2" class="attributes">
              <v-icon size="large" icon="mdi-arrow-up-down" color=""></v-icon>

              <v-checkbox v-model="element.pkey">PK</v-checkbox>
              <v-checkbox v-model="element.fkey">FK</v-checkbox>
              <v-text-field v-model="element.name" label="Attribut Name" variant="filled" class="input" required :hide-details="true" />

              <v-icon size="large" icon="mdi-delete" color="error" @click="deleteAttribute(element)"></v-icon>
            </v-card>
          </template>
        </draggable>
      </v-card-text>

      <v-card-actions>
        <v-btn block @click="closeModal()">Schließen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import draggable from "vuedraggable";
import { useToolManagementStore } from "../stores/toolManagementStore";
import { useDiagramStore } from "@/stores/diagramStore";

import Attribute from "../model/diagram/Attribute";

const toolManagementStore = useToolManagementStore();
const diagramStore = useDiagramStore();

const drag: any = ref();
const dialog = ref<boolean>(false);
const newAttributeName = ref<string>("");

toolManagementStore.$subscribe((mutation, state) => {
  if (state.showModalAddAttributes != undefined) {
    dialog.value = state.showModalAddAttributes;
  }
});

const deleteAttribute = (attribute: Attribute) => {
  //console.log(props.entity.attributes)
  if (toolManagementStore.selectedEntity != undefined) {
    let index = toolManagementStore.selectedEntity.attributes.indexOf(attribute);
    toolManagementStore.selectedEntity.attributes.splice(index, 1);

    diagramStore.saveHistory();
  }
};

const addAttribute = () => {
  //validate input
  if (newAttributeName.value == "") {
    //TODO: show error message
    return;
  }

  if (toolManagementStore.selectedEntity != undefined) {
    toolManagementStore.selectedEntity.attributes.push({
      name: newAttributeName.value,
      pkey: false,
      fkey: false,
    });
    newAttributeName.value = "";
    diagramStore.saveHistory();
  }
};

const closeModal = () => {
  toolManagementStore.showModalAddAttributes = false;
};
</script>

<style scoped lang="scss">
.container {
  // center the dialog
  display: flex;
  justify-content: center;
  align-items: center;
  width: 600px;
}

.card-text {
  height: 370px;
  overflow-y: auto;
}

.imput-form {
  margin-bottom: 10px;
  display: grid;
  grid-template-columns: auto;
  align-items: center;
  justify-items: center;
  grid-gap: 10px 20px;

  .btn {
    grid-column: 1 / 2;
  }

  * {
    width: 100%;
  }
}

.draggable > .attributes {
  padding: 5px 0px;
  margin: 10px 0px;
  width: 100%;
  height: 80px;
  background-color: white;
  display: flex;
  align-items: center;
  justify-items: center;

  .v-text-field {
    width: 50%;
    margin: 5px;
  }
  .v-checkbox {
    margin: -20px;
    padding: 20px 0 0 10px;
    justify-items: center;
  }
}

.ghost {
  color: rgb(var(--v-theme-success));
}
</style>
