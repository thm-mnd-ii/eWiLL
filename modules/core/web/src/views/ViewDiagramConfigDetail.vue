<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title>
            <span>{{ diagramConfig.name }}</span>
            <v-chip class="ml-3 text-uppercase" variant="elevated" size="small">{{ diagramConfig.diagramType }}</v-chip>
          </v-card-title>
          <v-card-text>
            <p>Sample Text</p>
            <div class="align-items-center">
              <v-chip prepend-icon="mdi-account-circle" color="primary" label> Author - {{ diagramConfig.createdBy.firstName }} {{ diagramConfig.createdBy.lastName }} </v-chip>
              <v-chip prepend-icon="mdi-account-edit" class="ml-4" size="small"> Last Edit - {{ lastModifiedEUStr }}</v-chip>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12">
        <v-card>
          <v-tabs v-model="tab" class="tabs-header" color="primary" grow>
            <v-tab value="elements">
              <div class="d-flex flex-column align-center">
                <v-icon size="30" class="mt-1"> mdi-card-outline </v-icon>
                <span class="text-overline mt-n2">Elemente</span>
              </div>
            </v-tab>
            <v-tab value="connections">
              <div class="d-flex flex-column align-center">
                <v-icon size="30" class="mt-1"> mdi-transit-connection-variant </v-icon>
                <span class="text-overline mt-n2">Verbindungen</span>
              </div>
            </v-tab>
          </v-tabs>

          <v-card-text>
            <v-window v-model="tab">
              <v-window-item value="elements">
                <v-stepper v-model="step" :items="['Symbol hochladen', 'Step 2']" hide-actions>
                  <template #[`item.1`]>
                    <v-card title="Schritt Eins" flat>
                      <!-- show svg file -->
                      <ElementWrapper :svg-object="svgObject" height="100" />

                      <!-- input file -->
                      <v-file-input v-model="fileInput" :rules="fileRules" label="Icon hochladen" accept="image/svg+xml" validate-on="input" show-size earable @update:model-value="updateFile"></v-file-input>
                    </v-card>
                  </template>

                  <template #[`item.2`]>
                    <v-card title="Schritt Zwei" flat>...</v-card>
                  </template>

                  <v-stepper-actions @click:next="validateCurrentTab" @click:prev="step--" />
                </v-stepper>
              </v-window-item>

              <v-window-item value="connections"> connections </v-window-item>
            </v-window>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="6">
        <v-card>
          <v-card-title>Elemente</v-card-title>
          <v-card-text>
            <v-card v-for="element in diagramConfig.elementTypes" :key="element.id">
              <v-card-title>{{ element.name }}</v-card-title>
            </v-card>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="6">
        <v-card>
          <v-card-title>Verbindungen</v-card-title>
          <v-card-text>
            <v-card v-for="connection in diagramConfig.connectionTypes" :key="connection.id">
              <v-card-title>{{ connection.name }}</v-card-title>
            </v-card>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

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
import type UserPL from '@/model/UserPL'
import ElementWrapper from '@/components/modelingTool/ElementWrapper.vue'

const route = useRoute()

const isValidConfigId = ref<boolean>(false)

const snackbar = ref({
  show: false,
  text: '',
  color: '',
  timeout: 10000,
  top: false
})

const fileRules = ref([(v: File | null) => !!v || 'Datei ist erforderlich'])

const fileInput = ref<File[]>()

const svgObject = ref<SVGElement>(document.createElementNS('http://www.w3.org/2000/svg', 'svg'))

const tab = ref<number>(0)
const step = ref<number>(0)

const diagramConfigId = ref(Number(route.params.id))
const diagramConfig = ref<DiagramConfig>({
  createdBy: {} as UserPL
} as DiagramConfig)

const lastModifiedEUStr = ref<string>('')

onMounted(() => {
  if (diagramConfigId.value != undefined) {
    diagramConfigService
      .get(diagramConfigId.value)
      .then((response) => {
        diagramConfig.value = response.data
        lastModifiedEUStr.value = new Date(diagramConfig.value.lastModified).toLocaleString('de-DE')
        isValidConfigId.value = true
      })
      .catch((error) => {
        snackbar.value.show = true
        snackbar.value.text = `Keine Diagrammkonfiguration für die ID ${route.params.id} gefunden.`
        snackbar.value.color = 'error'

        console.error(error)
      })
  }
})

const updateFile = (files: File[]) => {
  if (files[0] && files[0].type === 'image/svg+xml') {
    const reader = new FileReader()
    reader.onload = (e) => {
      const parser = new DOMParser()
      const htmlElement = parser.parseFromString(e.target?.result as string, 'image/svg+xml').documentElement
      svgObject.value = htmlElement as unknown as SVGElement
    }
    reader.readAsText(files[0])
  } else {
    snackbar.value.show = true
    snackbar.value.text = 'Bitte laden Sie ein SVG-Bild hoch.'
    snackbar.value.color = 'error'
  }
}

const validateCurrentTab = () => {
  if (tab.value === 0) {
    if (fileInput.value && fileInput.value[0] && fileInput.value[0].type === 'image/svg+xml') {
      // do smthng
      step.value++
    } else {
      snackbar.value.show = true
      snackbar.value.text = 'Bitte laden Sie ein SVG-Bild hoch.'
      snackbar.value.color = 'error'
    }
  }
}
</script>

<style scoped>
.tabs-header {
  --v-tabs-height: 65px;
}
</style>
