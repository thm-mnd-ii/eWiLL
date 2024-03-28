<template>
  <div></div>

  <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="snackbar.timeout" :top="snackbar.top">
    {{ snackbar.text }}

    <template #actions>
      <v-btn color="white" variant="text" @click="snackbar.show = false"> Schließen </v-btn>
    </template>
  </v-snackbar>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

import type DiagramConfig from '@/model/diagram/DiagramConfig'
import diagramConfigService from '@/services/diagramConfig.service'

const route = useRoute()

const snackbar = ref({
  show: false,
  text: '',
  color: '',
  timeout: 10000,
  top: false
})

const diagramConfigId = ref(Number(route.params.id))
const diagramConfig = ref<DiagramConfig>({} as DiagramConfig)

onMounted(() => {
  if (diagramConfigId.value != undefined) {
    diagramConfigService
      .get(diagramConfigId.value)
      .then((response) => {
        diagramConfig.value = response.data
      })
      .catch((error) => {
        snackbar.value.show = true
        snackbar.value.text = `Keine Diagrammkonfiguration für die ID ${route.params.id} gefunden.`
        snackbar.value.color = 'error'

        console.error(error)
      })
  }
})
</script>

<style scoped></style>
