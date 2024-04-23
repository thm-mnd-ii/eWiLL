<template>
  <v-card>
    <v-card-title>
      <span class="text-h5">Import Diagram</span>
    </v-card-title>
    <v-card-text>
      <v-form ref="formImport" v-model="validImportForm" @submit.prevent>
        <v-file-input v-model="importFile" :rules="importFileRules" label="File*" required></v-file-input>

        <v-container>
          <v-row v-if="!importAsNew && !importOverride">
            <v-col cols="12" lg="6">
              <v-btn block @click="importAsNew = true">Import as a new Diagram</v-btn>
            </v-col>
            <v-col cols="12" lg="6">
              <v-btn block @click="importOverride = true">Override existing Diagram</v-btn>
            </v-col>
          </v-row>
          <v-row v-if="importAsNew || importOverride">
            <v-col v-if="importAsNew" cols="12">
              <p class="mb-2">Erstelle neues Diagramm</p>
              <v-text-field v-model="diagramName" :rules="[(v: any) => !!v || 'Item is required']" label="Name*" required></v-text-field>
              <v-select v-model="diagramCategory" :rules="[(v: any) => !!v || 'Item is required']" :items="categoryNames" item-title="name" item-value="id" label="Ordner*" required></v-select>
            </v-col>

            <v-col v-if="importOverride" cols="12">
              <p class="mb-2">Überschreibe bestehendes Diagramm</p>
              <v-select v-model="selectedCategoryId" :rules="[(v: any) => !!v || 'Item is required']" label="Ordner" variant="underlined" :items="categoryNames" item-title="name" item-value="id" @update:model-value="updateDiagrams"></v-select>
              <v-select v-model="selectedDiagramId" :rules="[(v: any) => !!v || 'Item is required']" label="Diagram" variant="underlined" :items="diagrams" item-title="name" item-value="id" :disabled="diagrams.length <= 0"></v-select>
            </v-col>

            <v-col cols="6">
              <v-btn v-if="importAsNew" type="submit" block @click="importDiagram">Erstellen</v-btn>
              <v-btn v-if="importOverride" type="submit" block @click="importDiagram">Überschreiben</v-btn>
            </v-col>
            <v-col cols="6">
              <v-btn
                block
                @click="
                  () => {
                    importAsNew = false
                    importOverride = false
                  }
                "
                >Cancel</v-btn
              >
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import type Category from '@/model/diagram/Category'
import type Diagram from '@/model/diagram/Diagram'
import { onMounted, ref } from 'vue'

import diagramService from '@/services/diagram.service'
import { useAuthUserStore } from '../stores/authUserStore'

const authUserStore = useAuthUserStore()

const formImport = ref()
const validImportForm = ref()

const importFile = ref<File[] | undefined>(undefined)
const importFileRules = ref<any[]>([(v: any) => !!v || 'File is required', (v: any) => (v && v.length > 0) || 'File is required', (v: any) => (v && v[0].type === 'application/json') || 'File must be a JSON file'])
const importAsNew = ref<boolean>(false)
const importOverride = ref<boolean>(false)

const diagramName = ref<string>('')
const diagramCategory = ref<number>()

const selectedCategoryId = ref<number>()
const selectedDiagramId = ref<number | null>()
const diagrams = ref<Diagram[]>([])
const categoryNames = ref<Category[]>([])

const emit = defineEmits(['close'])

onMounted(() => {
  updateCategories()
})

const updateCategories = () => {
  diagramService
    .getCategories(authUserStore.auth.user?.id as number)
    .then((response) => {
      categoryNames.value = response.data
    })
    .catch((error) => {
      console.log(error)
    })
}

const updateDiagrams = async (categoryId: number, resetSelection: boolean = true) => {
  await diagramService.getDiagramsByUserId(authUserStore.auth.user?.id as number).then((response) => {
    if (resetSelection) selectedDiagramId.value = undefined
    diagrams.value = response.data.filter((d) => d.categoryId == categoryId)
  })
}

const importDiagram = () => {
  formImport.value.validate().then(() => {
    if (validImportForm.value && importFile.value != null) {
      const file = importFile.value[0]
      const reader = new FileReader()
      reader.onload = (e) => {
        const content = e.target?.result
        if (content == null) {
          return
        }
        const diagram = JSON.parse(content as string)

        if (diagram.name == null || diagram.entities == null || diagram.connections == null) {
          alert('File is not a valid diagram file')
          return
        } else {
          saveDiagram(diagram)

          emit('close')
        }
      }
      reader.readAsText(file)
    }
  })
}

const saveDiagram = (diagram: Diagram) => {
  if (authUserStore.auth.user != null) {
    diagram.ownerId = authUserStore.auth.user?.id
  } else {
    throw new Error('User is not logged in')
  }

  if (importAsNew.value) {
    diagram.name = diagramName.value
    diagram.categoryId = diagramCategory.value as number

    // save diagram
    diagramService
      .postDiagram(diagram)
      .then((result) => {
        localStorage.removeItem('diagram')
      })
      .catch((error) => {
        console.log(error)
        alert('Diagramm konnte nicht gespeichert werden')
      })
  } else if (importOverride.value) {
    if (selectedDiagramId.value != null) {
      diagram.id = selectedDiagramId.value
      diagram.name = diagrams.value.find((d) => d.id == selectedDiagramId.value)?.name as string
      diagram.categoryId = selectedCategoryId.value as number

      diagramService
        .putDiagram(diagram)
        .then((result) => {
          localStorage.removeItem('diagram')
        })
        .catch((error) => {
          console.log(error)
          alert('Diagramm konnte nicht gespeichert werden')
        })
    }
  }
}
</script>
