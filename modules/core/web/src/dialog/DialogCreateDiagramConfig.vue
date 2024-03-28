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
          <v-btn v-if="!loading" color="primary" variant="flat" type="submit" @click="_confirm"> Weiter zur Konfiguration </v-btn>
          <v-progress-circular v-if="loading" color="primary" indeterminate size="40"></v-progress-circular>
        </v-card-actions>
      </v-form>
    </v-card>
    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="snackbar.timeout" :top="snackbar.top">
      {{ snackbar.text }}

      <template #actions>
        <v-btn color="white" variant="text" @click="snackbar.show = false"> Schließen </v-btn>
      </template>
    </v-snackbar>
  </v-dialog>
</template>

<script setup lang="ts">
import type DiagramConfig from '@/model/diagram/DiagramConfig'
import { ref } from 'vue'
import diagramConfigService from '@/services/diagramConfig.service'
import { useRouter } from 'vue-router'

const router = useRouter()

const createDiagramConfigDialog = ref<boolean>(false)

const diagramConfig = ref<DiagramConfig>({} as DiagramConfig)
const ruleName = [(v: string) => !!v || 'Name is required']
const loading = ref(false)
const valid = ref(false)

const snackbar = ref({
  show: false,
  text: '',
  color: '',
  timeout: 10000,
  top: false
})

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
    diagramConfigService
      .create(diagramConfig.value)
      .then((response) => {
        loading.value = false
        createDiagramConfigDialog.value = false
        diagramConfig.value = {} as DiagramConfig

        router.push('/diagramConfig/' + response.data.id)

        resolvePromise.value(response.data)
      })
      .catch((error) => {
        snackbar.value.show = true
        snackbar.value.text = 'Fehler beim Erstellen der Diagramm Konfiguration. Siehe Konsole für mehr Informationen.'
        snackbar.value.color = 'error'
        console.error(error)
        loading.value = false
      })
  } else {
    loading.value = false
  }
}

const _cancel = () => {
  createDiagramConfigDialog.value = false
  resolvePromise.value(undefined)
}

// define expose
defineExpose({
  openDialog
})
</script>

<style scoped></style>
