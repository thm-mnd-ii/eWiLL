<template>
  <v-dialog v-model="saveDialog">
    <v-container>
      <v-row>
        <v-col cols="12" md="6">
          <DiagramImport @close="_close()" />
        </v-col>
        <v-col cols="12" md="6">
          <DiagramExport @close="_close()" />
        </v-col>
      </v-row>
    </v-container>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import DiagramImport from '@/components/DiagramImport.vue'
import DiagramExport from '@/components/DiagramExport.vue'

const saveDialog = ref<boolean>(false)

// Promis
const resolvePromise: any = ref(undefined)
const rejectPromise: any = ref(undefined)

const openDialog = () => {
  saveDialog.value = true

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve
    rejectPromise.value = reject
  })
}

const _close = () => {
  saveDialog.value = false
  resolvePromise.value(false)
}

// define expose
defineExpose({
  openDialog
})
</script>

<style scoped lang="scss">
.save-diagram-form {
  // grid in one row
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr;
  grid-template-areas: 'name category category';
  grid-column-gap: 10px;
  grid-row-gap: 10px;
  // align items
  align-items: center;
  // set width
  width: 100%;
}
</style>
