<template>
  <v-dialog v-model="dialog" class="container">
    <v-card>
      <v-card-title> Attribute </v-card-title>
      <v-card-subtitle>
        Entität: <b>{{ props.entity.entityName }}</b>
      </v-card-subtitle>

      <v-card-text class="card-text">
        <v-form class="imput-form">
          <v-text-field v-model="newAttributeName" lable="Attribut Name" variant="solo" required hide-details="true" />

          <v-select v-model="selectedAttributeType" lable="Typ Auswählen" variant="solo" :items="attributeTypes" item-title="key" item-value="attribute" hide-details="true"></v-select>

          <v-btn class="btn" color="secondary" @click="addAttribute">Hinzufügen</v-btn>
        </v-form>

        <draggable v-model="props.entity.attributes" class="draggable" group="people" item-key="id" @start="drag = true" @end="drag = false">
          <template #item="{ element }">
            <v-card :elevation="2" class="attributes">
              <v-icon size="x-large" icon="mdi-arrow-up-down"></v-icon>

              <v-text-field v-model="element.name" lable="Attribut Name" variant="solo" required hide-details="true" />

              <v-select v-model="element.typ" lable="Typ Auswählen" variant="solo" :items="attributeTypes" item-title="key" item-value="attribute" hide-details="true"> </v-select>

              <v-icon size="x-large" icon="mdi-delete" @click="deleteAttribute(element)"></v-icon>
            </v-card>
          </template>
        </draggable>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" block @click="$emit('close')">Schließen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch, computed } from "vue";
import AttributeTyp from "../enums/AttributeTyp";
import draggable from "vuedraggable";

const props = defineProps({
  entity: { type: Object, required: true },
  show: { type: Boolean, required: true },
});

const emit = defineEmits(["close", "update:entity"]);

const dialog = ref(false);
const newAttributeName = ref("");
const selectedAttributeType = ref("");

watch(
  () => props.entity,
  (entity) => {
    emit("update:entity", entity.value);
  }
);

watch(
  () => props.show,
  (show) => {
    dialog.value = show;
  }
);

const attributeTypes = computed({
  get() {
    return Object.keys(AttributeTyp).map((key) => {
      return { attribute: AttributeTyp[key], key: key };
    });
  },
});

const deleteAttribute = (attribute) => {
  //console.log(props.entity.attributes)
  let index = props.entity.attributes.indexOf(attribute);
  props.entity.attributes.splice(index, 1);
};

const addAttribute = () => {
  console.log(newAttributeName.value, selectedAttributeType.value);

  props.entity.attributes.push({ typ: selectedAttributeType.value, name: newAttributeName.value });

  newAttributeName.value = "";
  selectedAttributeType.value = "";
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
