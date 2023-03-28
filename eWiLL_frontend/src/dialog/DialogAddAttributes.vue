<template>
  <v-dialog v-if="toolManagementStore.selectedEntity != undefined" v-model="dialog" class="container">
    <v-card>
      <v-card-title> Attribute </v-card-title>
      <v-card-subtitle>
        Entität: <b>{{ toolManagementStore.selectedEntity.entityName }}</b>
      </v-card-subtitle>

      <v-card-text class="card-text">
        <v-form class="imput-form">
          <v-text-field v-model="newAttributeName" lable="Attribut Name" variant="solo" required :hide-details="true" />

          <v-select v-model="selectedAttributeType" lable="Typ Auswählen" variant="solo" :items="attributeTypes" item-title="key" item-value="attribute" :hide-details="true"></v-select>

          <v-btn class="btn" color="secondary" @click="addAttribute">Hinzufügen</v-btn>
        </v-form>

        <draggable v-model="toolManagementStore.selectedEntity.attributes" class="draggable" group="people" item-key="id" @start="drag = true" @end="drag = false">
          <template #item="{ element }">
            <v-card :elevation="2" class="attributes">
              <v-icon size="x-large" icon="mdi-arrow-up-down"></v-icon>

              <v-text-field v-model="element.name" lable="Attribut Name" variant="solo" required :hide-details="true" />

              <v-select v-model="element.type" lable="Typ Auswählen" variant="solo" :items="attributeTypes" item-title="key" item-value="attribute" :hide-details="true"> </v-select>

              <v-icon size="x-large" icon="mdi-delete" @click="deleteAttribute(element)"></v-icon>
            </v-card>
          </template>
        </draggable>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" block @click="closeModal()">Schließen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import draggable from "vuedraggable";
import { useToolManagementStore } from "../stores/toolManagementStore";
import { useDiagramStore } from "@/stores/diagramStore";

import AttributeType from "../enums/AttributeType";
import Attribute from "../model/diagram/Attribute";

const toolManagementStore = useToolManagementStore();
const diagramStore = useDiagramStore();

const drag: any = ref();
const dialog = ref<boolean>(false);
const newAttributeName = ref<string>("");
const selectedAttributeType = ref<AttributeType>(AttributeType.Attribute);

toolManagementStore.$subscribe((mutation, state) => {
  if (state.showModalAddAttributes != undefined) {
    dialog.value = state.showModalAddAttributes;
  }
});

const attributeTypes = computed(() => {
  const keys = Object.keys(AttributeType).filter((k) => typeof AttributeType[k as any] === "number");
  const values = keys.map((k) => AttributeType[k as any]);
  const types: { key?: string; attribute?: number }[] = [];
  //values in array of objects
  keys.forEach((key, index) => {
    types[index] = { key: key, attribute: parseInt(values[index]) };
  });

  return types;
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
    toolManagementStore.selectedEntity.attributes.push({ type: selectedAttributeType.value, name: newAttributeName.value });

    newAttributeName.value = "";
    selectedAttributeType.value = AttributeType.Attribute;

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
  grid-template-columns: 3fr 2fr;
  align-items: center;
  justify-items: center;
  grid-gap: 10px 20px;

  .btn {
    grid-column: 1 / 3;
  }

  * {
    width: 100%;
  }
}

.draggable > .attributes {
  padding: 5px 0px;
  margin: 10px 0px;
  width: 100%;
  height: auto;
  background-color: lavender;
  display: grid;
  grid-template-columns: 1fr 7fr 4fr 1fr;
  align-items: center;
  justify-items: center;
}

.draggable > .attributes > * {
  width: 100%;
  padding: 0px 5px;
}
</style>
