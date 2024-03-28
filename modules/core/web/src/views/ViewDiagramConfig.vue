<template>
  <DialogCreateDialogConfig ref="dialogCreateDiagramConfig"></DialogCreateDialogConfig>

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
</template>

<script setup lang="ts">
import { ref } from 'vue'
import DialogCreateDialogConfig from '@/dialog/DialogCreateDiagramConfig.vue'

const dialogCreateDiagramConfig = ref<typeof DialogCreateDialogConfig>()

const search = ref('')

const headers = [
  { title: 'Name', key: 'name' },
  { title: 'Beschreibung', key: 'description' },
  { title: 'Aktionen', key: 'actions', sortable: false }
]

const items = ref([
  { name: 'Diagramm 1', description: 'Beschreibung 1' },
  { name: 'Diagramm 2', description: 'Beschreibung 2' },
  { name: 'Diagramm 3', description: 'Beschreibung 3' }
])

const editItem = (item: any) => {
  console.log('Edit Item', item)
}

const deleteItem = (item: any) => {
  console.log('Delete Item', item)
}

const createNewDiagramConfig = () => {
  if (dialogCreateDiagramConfig.value) {
    dialogCreateDiagramConfig.value.openDialog().then((response: boolean) => {
      console.log('Response', response)
    })
  }
}
</script>

<style scoped lang="scss"></style>
