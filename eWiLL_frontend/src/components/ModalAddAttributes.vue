<template>
    <div class="vue-modal">
        <div class="vue-modal-inner">
            <div class="vue-modal-content">
              <h3>{{ props.entity.entityName }}</h3>
              
              <div class="newAttributeContainer">
                <input v-model="newAttributeName" type="text" class="form-control" placeholder="New Attribute">
                
                <select v-model="selectedAttributeType" class="form-select" name="attributeType">
                  <option disabled value="">Select Typ</option>
                  <option v-for="key, attribute in AttributeTyp" :key="key" :value="key">
                    {{ attribute }}
                  </option>
                </select>

                <button type="submit" class="btn btn-primary" @click="addAttribute">Add</button>
              </div>

              <draggable v-model="props.entity.attributes" class="draggable" group="people" @start="drag=true" @end="drag=false" item-key="id">
                <template #item="{element}">
                  <div class="attributes">
                    <IconMoveNorthSouth class="icon-move"/>
                    <input v-model="element.name" class="form-control" type="text">

                    <select v-model="element.typ" class="form-select" name="attributeType">
                      <option v-for="key, attribute in AttributeTyp" :key="key" :value="key" >
                        {{ attribute }}
                      </option>
                    </select>
                    
                    <button type="button" class="btn btn-danger" @click="deleteAttribute(element)">Delete</button>
                  </div>
                </template>
              </draggable>

                <button type="button" class="btn btn-success" @click="$emit('close')">Close</button>
            </div>
        </div>
    </div>
</template>

<script setup>
  import { ref, watch } from 'vue'
  import AttributeTyp from "../enums/AttributeTyp"
  import draggable from 'vuedraggable'
  import IconMoveNorthSouth from "./icons/IconMoveNorthSouth.vue"

  const props = defineProps(['entity'])
  
  const emit = defineEmits(['close', 'update:entity'])

  const newAttributeName = ref("")
  const selectedAttributeType = ref("")

  watch(props.entity, (entity) => {
        emit('update:entity', entity.value)
  })

  const deleteAttribute = (attribute) => {
    //console.log(props.entity.attributes)
    let index = props.entity.attributes.indexOf(attribute)
    props.entity.attributes.splice(index, 1)
  }

  const addAttribute = () => {
    console.log(newAttributeName.value, selectedAttributeType.value)
    
    props.entity.attributes.push( { "typ": selectedAttributeType.value, "name": newAttributeName.value} )

    newAttributeName.value = ""
    selectedAttributeType.value = ""
  }
</script>

<style scoped>

*,
::before,
::after {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.vue-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 10;
}

.vue-modal-inner {
  overflow-x: hidden;
  overflow-y: auto;
  max-width: 600px;
  max-height: 600px;
  margin: 2rem auto;
}

.vue-modal-content {
  position: relative;
  background-color: #fff;
  border: 1px solid rgba(0, 0, 0, 0.3);
  background-clip: padding-box;
  border-radius: 0.3rem;
  padding: 1rem;
}

.newAttributeContainer {
  border-radius: 5px;
  background: rgb(196, 196, 214);
  padding: 5px 0px 5px 30px;
  margin: 10px 0px 20px 0px;
  width: 100%;
  height: auto;

  display: flex;
  align-items: center;
  justify-content: start;
}

.newAttributeContainer > * {
  margin: 0px 5px;
}

.draggable > .attributes {
  border-radius: 5px;
  background: lavender;
  padding: 5px 0px;
  margin: 5px 0px;
  width: 100%;
  height: auto;

  display: flex;
  align-items: center;
  justify-content: start;
}

.draggable > .attributes > * {
  margin: 0px 5px;
}

.icon-move {
  width: 70px;
  fill: rgb(51, 51, 51);
}

</style>