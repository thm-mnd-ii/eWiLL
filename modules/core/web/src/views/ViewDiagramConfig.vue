<template>
  <DialogCreateDialogConfig ref="dialogCreateDiagramConfig"></DialogCreateDialogConfig>
  <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>

  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title>Alle Diagramme</v-card-title>
          <v-card-text>
            <v-row>
              <v-col cols="12">
                <v-text-field v-model="search" label="Suche" outlined dense></v-text-field>
              </v-col>

              <v-col cols="12">
                <v-row justify="center">
                  <v-btn color="primary" @click="createNewDiagramConfig">Neues Diagramm</v-btn>
                </v-row>
              </v-col>

              <v-col cols="12">
                <v-data-table :headers="headers" :items="items" :search="search" :items-per-page="5" class="elevation-1">
                  <template #[`item.actions`]="{ item }">
                    <v-tooltip location="top">
                      <template #activator="{ props }">
                        <v-btn icon variant="plain" v-bind="props" @click="editItem(item)">
                          <v-icon> mdi-pencil </v-icon>
                        </v-btn>
                      </template>
                      <span>Bearbeiten</span>
                    </v-tooltip>

                    <v-tooltip location="top">
                      <template #activator="{ props }">
                        <v-btn icon variant="plain" v-bind="props" @click="deleteItem(item)">
                          <v-icon> mdi-delete </v-icon>
                        </v-btn>
                      </template>
                      <span>Löschen</span>
                    </v-tooltip>
                  </template>
                </v-data-table>
              </v-col>
            </v-row>
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import DialogCreateDialogConfig from '@/dialog/DialogCreateDiagramConfig.vue'
import DialogConfirmVue from '@/dialog/DialogConfirm.vue'
import type DiagramConfig from '@/model/diagram/DiagramConfig'
import diagramConfigService from '@/services/diagramConfig.service'

const router = useRouter()

const dialogCreateDiagramConfig = ref<typeof DialogCreateDialogConfig>()
const dialogConfirm = ref<typeof DialogConfirmVue>()

const search = ref('')

const snackbar = ref({
  show: false,
  text: '',
  color: '',
  timeout: 10000,
  top: false
})

const headers = [
  { title: 'Name', key: 'name' },
  { title: 'Typ', key: 'diagramType' },
  { title: 'Aktionen', key: 'actions', sortable: false }
]

const items = ref<DiagramConfig[]>([])

onMounted(() => {
  diagramConfigService
    .getAll()
    .then((response) => {
      items.value = response.data
    })
    .catch((error) => {
      console.error(error)
    })
})

const editItem = (item: any) => {
  router.push('/diagramConfig/' + item.id)
}

const deleteItem = (item: any) => {
  dialogConfirm.value.openDialog('Diagrammkonfiguration löschen', 'Möchten Sie die Diagrammkonfiguration wirklich löschen?').then((response: boolean) => {
    if (response) {
      diagramConfigService
        .delete(item.id)
        .then(() => {
          items.value = items.value.filter((i) => i.id != item.id)
        })
        .catch((error) => {
          snackbar.value.show = true
          snackbar.value.text = 'Fehler beim Löschen der Diagrammkonfiguration.'
          snackbar.value.color = 'error'
          console.error(error)
        })
    }
  })
}

const createNewDiagramConfig = () => {
  if (dialogCreateDiagramConfig.value) {
    dialogCreateDiagramConfig.value.openDialog().then((response: DiagramConfig) => {
      if (response != undefined) {
        items.value.push(response)
      }
    })
  }
}
</script>

<style scoped lang="scss"></style>
