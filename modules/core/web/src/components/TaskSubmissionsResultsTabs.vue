<template>
  <v-card class="task-trials-tabs">
    <v-tabs v-model="selectedResultTab" bg-color="teal-darken-3" slider-color="teal-lighten-3">
      <v-tab v-for="tab in submissions" :key="tab.attempt" :value="tab.attempt">
        {{ 'Ergebnis ' + tab.attempt }}
      </v-tab>
    </v-tabs>
    <v-window v-model="selectedResultTab">
      <v-window-item v-for="tab in submissions" :key="tab.attempt" :value="tab.attempt">
        <v-card flat>
          <v-card-text class="task-trials-text">
            <v-chip v-if="selectedResult.correct" color="green" append-icon="mdi-check-circle" class="margin-right-5px">Korrekt: </v-chip>
            <v-chip v-if="!selectedResult.correct" color="red" append-icon="mdi-close-circle" class="margin-right-5px">Fehlerhaft: </v-chip>
            <v-chip v-if="selectedResult.correct" color="green">Score: {{ selectedResult.score }}</v-chip>
            <v-chip v-if="!selectedResult.correct" color="red">Score: {{ selectedResult.score }}</v-chip>
            <br />
            <br />
            <!-- <p v-for="comment in selectedResult.comments" :key="comment.message">{{ comment.message }}</p> -->
            <v-data-table :group-by="groupBy" :headers="headers" :items="selectedResult.comments" :sort-by="sortBy" class="elevation-1" item-value="name" :single-select="true" @click:row="(event: any, item: any) => onRowClick(item)">
              <template #[`item.statusLevel`]="{ item }">
                <v-icon v-if="item.statusLevel == 'SUGGESTION'" size="large" :color="getHighlightLevelColor(item.statusLevel)">mdi-information</v-icon>
                <v-icon v-if="item.statusLevel == 'INCORRECT'" size="large" :color="getHighlightLevelColor(item.statusLevel)">mdi-close-circle</v-icon>
                <v-icon v-if="item.statusLevel == 'CORRECT'" size="large" :color="getHighlightLevelColor(item.statusLevel)">mdi-check-circle</v-icon>
              </template>
            </v-data-table>
          </v-card-text>
          <!-- <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn class="" append-icon="mdi-open-in-new" color="dark-gray" variant="text" @click="showDiagramWithMistakes"> Zeige Fehler im Diagram </v-btn>
          </v-card-actions> -->
        </v-card>
      </v-window-item>
    </v-window>
  </v-card>
  <DialogShowFullDiagram ref="dialogShowFullDiagram" />
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAuthUserStore } from '../stores/authUserStore'
import { useDiagramStore } from '@/stores/diagramStore'
// Models
// Models
import type Submission from '../model/submission/Submission'
import type Diagram from '../model/diagram/Diagram'
import type Task from '../model/task/Task'
// Services
import submissionService from '@/services/submission.service'
//Dialogs
import DialogShowFullDiagram from '@/dialog/DialogShowFullDiagram.vue'
import type Result from '@/model/submission/Result'
import { useToolManagementStore } from '@/stores/toolManagementStore'
import { toRaw } from 'vue'
import FeedbackLevel from '@/enums/FeedbackLevel'
import type { VDataTable } from 'vuetify/components'
type ReadonlyHeaders = VDataTable['headers']
type ReadonlyGroupBy = VDataTable['groupBy']
type ReadonlySortBy = VDataTable['sortBy']

const toolManagementStore = useToolManagementStore()
const authUserStore = useAuthUserStore()
const diagramStore = useDiagramStore()
const userId = ref(authUserStore.auth.user?.id!)

const currentTask = ref<Task>({} as Task)

const selectedResultTab = ref<number>(1)
const dialogShowFullDiagram = ref<typeof DialogShowFullDiagram>()

const submissions = ref<Submission[]>([] as Submission[])
const selectedResult = ref<Result>({} as Result)

const sortBy = ref<ReadonlySortBy>([{ key: 'feedbackLevel', order: 'asc' }])
const groupBy = ref<ReadonlyGroupBy>([])
const headers = ref<ReadonlyHeaders>([
  { title: 'Status', key: 'feedbackLevel' },
  { title: 'Info', key: 'message' },
  { title: 'Entity', key: 'affectedEntityName' },
  { title: 'Attribut', key: 'affectedAttributeName' },
  { title: 'Level', key: 'statusLevel' },
  { title: 'Info Type', key: 'resultMessageType' }
])

watch(
  () => selectedResultTab.value,
  (newValue) => {
    submissionService.getResultBySubmissionIdAndLevel(submissions.value[newValue - 1].id, currentTask.value.showLevel).then((response) => {
      selectedResult.value = response.data
    })
  }
)

const load = (task: Task, selectedSubmissionIndex?: number) => {
  currentTask.value = task

  submissionService.getSubmissionIdsByUserAndTask(userId.value, task.id).then((response) => {
    submissions.value = response.data

    //select last tab
    selectedResultTab.value = selectedSubmissionIndex !== undefined ? selectedSubmissionIndex : submissions.value.length

    //if first submission, load result for first submission
    if (submissions.value.length == 1) {
      submissionService.getResultBySubmissionIdAndLevel(submissions.value[submissions.value.length - 1].id, currentTask.value.showLevel).then((response) => {
        selectedResult.value = response.data
      })
    }
  })
}

const showDiagramWithMistakes = () => {
  diagramStore.loadDiagram(submissions.value[selectedResultTab.value - 1].diagram as Diagram)
  dialogShowFullDiagram.value?.openDialog('')
}

const getHighlightLevelColor = (highlightLevel: string) => {
  if (highlightLevel == 'INCORRECT') {
    return 'red'
  } else if (highlightLevel == 'CORRECT') {
    return 'green'
  } else if (highlightLevel == 'SUGGESTION') {
    return 'blue'
  } else {
    return 'gray'
  }
}

const onRowClick = (item: any) => {
  /**
   * Workaround to retrieve the data contained inside the row
   * this method should be changed if a better alternative is found
   */
  const comment = toRaw(item.item)
  const entityId = Number(comment.affectedEntityId)
  // problem remains with -1 on multiple entities missing or not required !
  if (entityId == null || entityId == -1 || comment.feedbackLevel == FeedbackLevel.BASIC) {
    toolManagementStore.highlightedEntityId = null
    return
  } else {
    toolManagementStore.highlightedEntityId = entityId
  }
}

defineExpose({
  load
})
</script>

<style scoped>
.task-trials-tabs {
  margin: 0;
  width: 100%;
  height: 100%;
}

.task-trials-text {
  min-height: 100px;
}

.margin-right-5px {
  margin-right: 5px;
}

.table {
  width: 100%;

  tr {
    td {
      padding: 5px;
    }
  }
}
</style>
