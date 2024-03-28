<template>
  <v-dialog v-model="createDiagramConfigDialog" width="500px">
    <v-card>
      <v-card-title>Neue Diagramm Konfiguration</v-card-title>
      <v-form v-model="valid" @submit.prevent>
        <v-card-text>
          <v-text-field v-model="diagramConfig.name" :rules="ruleName" label="Diagramm Name"></v-text-field>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" variant="flat" @click="_cancel"> Abbrechen </v-btn>
          <v-btn v-if="!loading" color="primary" variant="flat" type="submit" @click="_confirm"> Speichern </v-btn>
          <v-progress-circular v-if="loading" color="primary" indeterminate size="40"></v-progress-circular>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import type DiagramConfig from '@/model/diagram/DiagramConfig'
import { ref } from 'vue'

const createDiagramConfigDialog = ref<boolean>(false)

const diagramConfig = ref<DiagramConfig>({} as DiagramConfig)
const ruleName = [(v: string) => !!v || 'Name is required']
const loading = ref(false)
const valid = ref(false)

// Promis
const resolvePromise: any = ref(undefined)
const rejectPromise: any = ref(undefined)

const openDialog = () => {
  createDiagramConfigDialog.value = true

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve
    rejectPromise.value = reject
  })
}

const _confirm = () => {
  loading.value = true

  if (valid.value) {
    createDiagramConfigDialog.value = false
    resolvePromise.value(true)
  } else {
    loading.value = false
  }
}

const _cancel = () => {
  createDiagramConfigDialog.value = false
  resolvePromise.value(false)
}

// define expose
defineExpose({
  openDialog
})
</script>

<style scoped></style>
