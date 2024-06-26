<template>
  <TaskDateVChip ref="taskDateVChip" class="hide" :due-date-prop="task.dueDate"></TaskDateVChip>

  <DialogShowFullDiagram ref="dialogShowFullDiagram" />
  <DialogEditTask ref="dialogEditTask" />
  <DialogConfirm ref="dialogConfirm" />

  <div class="task">
    <TaskVCard :course-role="courseRole" @task-updated="loadTask"></TaskVCard>

    <div v-if="courseRole != 'STUDENT'" class="switchRole">
      <!-- btn switch to student mode -->
      <v-btn class="switch-btn" color="info" variant="flat" @click="loadElements(CourseRoles.STUDENT)">Zur Studentenansicht wechseln</v-btn>
    </div>

    <div class="task-main">
      <div>
        <div class="grid-left">
          <v-btn v-if="selectedDiagramId == undefined" text-align="center" color="dark-gray" variant="flat" :disabled="submissionCount >= task.maxSubmissions || isDue" @click="createDiagramAndCategoryIfNotPresent">Diagramm erstellen & bearbeiten</v-btn>
          <v-btn v-if="selectedDiagramId != undefined" color="dark-gray" variant="flat" :disabled="submissionCount >= task.maxSubmissions || isDue" @click="loadViewModellingWithDiagram">Diagramm bearbeiten</v-btn>
        </div>
        <v-form>
          <v-select v-model="selectedCategoryId" label="Ordner" variant="underlined" :items="categories" item-title="name" :disabled="courseRole != 'STUDENT'" item-value="id" @update:model-value="updateDiagrams"></v-select>
          <v-select v-model="selectedDiagramId" label="Diagram" variant="underlined" :items="diagrams" item-title="name" item-value="id" :disabled="courseRole != 'STUDENT'" @update:model-value="showSelectedDiagram"></v-select>
        </v-form>

        <v-card class="preview-container">
          <v-card-title class="task-header-title">
            <h3 class="headline mb-0">Preview</h3>
            <v-spacer></v-spacer>
            <v-btn icon variant="text" color="dark-gray" @click="openFullDiagram">
              <v-icon icon="mdi-fullscreen" size="x-large"></v-icon>
            </v-btn>
          </v-card-title>
          <v-card-text class="modeling-container">
            <ModelingTool :key="modelingToolKey" class="modelPreview" :is-editable="false"></ModelingTool>
          </v-card-text>
        </v-card>
      </div>
      <div v-if="courseRole == 'STUDENT'" class="grid-right">
        <v-btn class="submit-btn" color="dark-gray" variant="flat" :disabled="submissionCount >= task.maxSubmissions || isDue" @click="submitDiagram">
          <div v-if="!subBtnProgress">
            <span>Einreichen</span>
          </div>
          <div v-if="subBtnProgress">
            <v-progress-circular indeterminate></v-progress-circular>
          </div>
        </v-btn>
        <br />
        <div class="task-trials-caption font-weight-medium">
          <span>Auswertungsergebnisse</span>
          <span>Anzahl Abgaben: {{ submissionCount }} / {{ task.maxSubmissions }}</span>
        </div>
        <TaskSubmissionsResultsTabs ref="taskSubmissionsResultsTabs"></TaskSubmissionsResultsTabs>
      </div>
      <div v-if="courseRole != 'STUDENT'" class="grid-right">
        <h3>Abgaben: {{ submissionCount }}</h3>
        <br />
        <v-btn @click="openViewTaskSubmissions">Zu den Abgaben</v-btn>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthUserStore } from '../stores/authUserStore'
import courseService from '../services/course.service'
import taskService from '../services/task.service'
import categoryService from '../services/category.service'
import type Task from '../model/task/Task'
import type SubmitPL from '../model/SubmitPL'
import DialogEditTask from '@/dialog/DialogEditTask.vue'
import DialogConfirm from '@/dialog/DialogConfirm.vue'

import type Category from '@/model/diagram/Category'
import type Diagram from '@/model/diagram/Diagram'
import DialogShowFullDiagram from '@/dialog/DialogShowFullDiagram.vue'
import diagramService from '@/services/diagram.service'
import evaluationService from '@/services/evaluation.service'
import { useDiagramStore } from '@/stores/diagramStore'
import { storeToRefs } from 'pinia'
import { useToolManagementStore } from '@/stores/toolManagementStore'
import ModelingTool from '@/components/ModelingTool.vue'
import submissionService from '@/services/submission.service'
import CourseRoles from '@/enums/CourseRoles'
import FeedbackLevel from '@/enums/FeedbackLevel'
import TaskDateVChip from '@/components/TaskDateVChip.vue'

import TaskSubmissionsResultsTabs from '@/components/TaskSubmissionsResultsTabs.vue'
import TaskVCard from '@/components/TaskVCard.vue'

const taskDateVChip = ref<typeof TaskDateVChip>()

const taskSubmissionsResultsTabs = ref<typeof TaskSubmissionsResultsTabs>()

const route = useRoute()
const router = useRouter()
const authUserStore = useAuthUserStore()
const diagramStore = useDiagramStore()
const toolManagementStore = useToolManagementStore()
const modelingToolKey = storeToRefs(diagramStore).key

const task = ref<Task>({} as Task)
const taskId = ref(Number(route.params.taskId))
const courseId = ref(Number(route.params.courseId))
const courseRole = ref('')
const userId = ref(authUserStore.auth.user?.id!)

const dialogEditTask = ref<typeof DialogEditTask>()
const dialogShowFullDiagram = ref<typeof DialogShowFullDiagram>()
const dialogConfirm = ref<typeof DialogConfirm>()

const categories = ref<Category[]>([])
const selectedCategoryId = ref<number>()
const diagrams = ref<Diagram[]>([])
const selectedDiagramId = ref<number | null>()
const selectedDiagram = ref<Diagram>()

const subBtnProgress = ref<boolean>(false)

//const submissions = ref();
const submissionCount = ref(0)

const isDue = ref(false)

// TODO: refactor in service and delete hidden component
watch(
  () => task.value.dueDate,
  (newVal) => {
    if (newVal) {
      isDue.value = taskDateVChip.value?.setDueDate(newVal) < 0
    }
  }
)

onMounted(() => {
  init()
})

const init = () => {
  courseService.getUserRoleInCourse(userId.value!, courseId.value).then((role) => {
    if (role == CourseRoles.NONE) {
      router.push('/course/' + route.params.courseId + '/signup')
    } else {
      loadElements(role)
    }
  })
}

const loadElements = async (role: CourseRoles) => {
  courseRole.value = role
  await loadTask()
  await loadCategories()

  if (courseRole.value == CourseRoles.STUDENT) {
    diagramStore.createNewDiagram()
    selectedCategoryId.value = undefined
    selectedDiagramId.value = undefined
    diagrams.value = []
    categories.value = []
    await loadDiagramIfExists()
    await loadSubmissions()
  } else if (courseRole.value == CourseRoles.OWNER || courseRole.value == CourseRoles.TUTOR) {
    await loadNumberSubmissions()
  }
}

const loadTask = () => {
  taskService.getTask(taskId.value).then((response) => {
    task.value = response
    if (courseRole.value != 'STUDENT') loadSolutionModel()
  })
}

const loadSubmissions = () => {
  evaluationService.getSubmissionIdsByUserAndTask(userId.value, taskId.value).then((response) => {
    const submissionIds = response.data
    submissionCount.value = submissionIds.length
    if (submissionCount.value > 0) taskSubmissionsResultsTabs.value!.load(task.value)
  })
}

const openFullDiagram = () => {
  dialogShowFullDiagram.value?.openDialog('')
}

const updateDiagrams = async (categoryId: number, resetSelection: boolean = true) => {
  await diagramService.getDiagramsByUserId(authUserStore.auth.user?.id as number).then((response) => {
    if (resetSelection) selectedDiagramId.value = undefined
    diagrams.value = response.data.filter((d) => d.categoryId == categoryId)
  })
}

const showSelectedDiagram = (diagramId: number | null) => {
  selectedDiagram.value = diagrams.value.find((d) => d.id == diagramId)

  let diagram = diagrams.value.find((d) => d.id == diagramId) as Diagram
  if (diagram != undefined) {
    diagramStore.loadDiagram(diagram)
  } else {
    console.log('Diagram not found')
  }
}

const submitDiagram = () => {
  if (selectedDiagramId.value == undefined) {
    dialogConfirm.value?.openDialog('Abgabe', 'Bitte wählen Sie ein Diagramm aus.', 'OK')
  } else {
    dialogConfirm.value?.openDialog('Abgabe: ' + selectedDiagram.value!.name, 'Möchten Sie das Diagram wirklich einreichen?', 'Einreichen').then((result: boolean) => {
      if (result) {
        const submitPL = {} as SubmitPL
        submitPL.diagramId = selectedDiagramId.value!
        submitPL.taskId = taskId.value
        submitPL.userId = userId.value
        evaluationService.submitDiagram(submitPL).then((submissionId) => {
          subBtnProgress.value = true

          waitUntilSubmissionIsEvaluated(submissionId.data).then(() => {
            subBtnProgress.value = false
            loadSubmissions()
          })
        })
      }
    })
  }
}

const waitUntilSubmissionIsEvaluated = (submissionId: number) => {
  return new Promise((resolve) => {
    const interval = setInterval(() => {
      evaluationService.getSubmissionById(submissionId, FeedbackLevel.NOTHING).then((response) => {
        if (response.status == 200) {
          clearInterval(interval)
          resolve(response.data)
        }
      })
    }, 1000)
  })
}

const loadCategories = () => {
  categoryService.getByUserId(userId.value).then((data) => {
    categories.value = data
  })
}

const loadDiagramIfExists = () => {
  courseService.getCourse(courseId.value).then((course) => {
    let courseName = course.data.name
    let diagramName = task.value.name

    categoryService.getByUserId(userId.value).then((categories) => {
      let diagramCategory: Category | undefined
      diagramCategory = categories.find((category) => category.name == courseName)

      if (diagramCategory != undefined) {
        diagramService.getDiagramsByUserId(userId.value).then(async (userDiagrams) => {
          let taskDiagram = userDiagrams.data.find((diagram) => diagram.name == diagramName && diagram.categoryId == diagramCategory!.id)

          if (taskDiagram != undefined) {
            selectedCategoryId.value = diagramCategory?.id
            await updateDiagrams(diagramCategory?.id as number, false)

            selectedDiagramId.value = taskDiagram.id
            showSelectedDiagram(taskDiagram.id)
          }
        })
      }
    })
  })
}

const loadSolutionModel = () => {
  if (task.value.solutionModelId == null) return
  if (selectedDiagramId.value == null) return

  diagramService.getDiagramById(task.value.solutionModelId).then((response) => {
    const categoryId = response.data.categoryId
    selectedCategoryId.value = categoryId
    diagrams.value.push(response.data)
    selectedDiagramId.value = task.value.solutionModelId
    showSelectedDiagram(selectedDiagramId.value)
  })
}

const openViewTaskSubmissions = () => {
  router.push(route.path + '/submissions')
}

const loadNumberSubmissions = () => {
  submissionService.getSubmissionsByTask(taskId.value).then((response) => {
    submissionCount.value = response.data.length
  })
}

// This was done really quick and dirty because it should be done in the backend, but has to be done here temporary
// TODO: Rewrite this cancer when the backend is ready
const createDiagramAndCategoryIfNotPresent = () => {
  courseService.getCourse(courseId.value).then((course) => {
    let courseName = course.data.name

    categoryService.getByUserId(userId.value).then((categories) => {
      let courseCategory: Category | undefined
      courseCategory = categories.find((category) => category.name == courseName)
      if (courseCategory === undefined) {
        diagramService.postCategory(courseName, userId.value).then((newCategory) => {
          courseCategory = newCategory.data
          createDiagram(courseCategory)
        })
      } else {
        createDiagram(courseCategory)
      }
    })
  })
}

const createDiagram = (category: Category) => {
  diagramService.getDiagramsByUserId(userId.value).then((userDiagrams) => {
    let taskDiagram = userDiagrams.data.find((diagram) => diagram.name == task.value.name && diagram.categoryId == category.id)
    if (taskDiagram === undefined) {
      diagramStore.createNewDiagram()
      diagramStore.diagram.name = task.value.name
      diagramStore.diagram.categoryId = category.id
      diagramStore.diagram.ownerId = userId.value

      diagramService.postDiagram(diagramStore.diagram).then((diagramId) => {
        localStorage.removeItem('diagram')
        diagramService.getDiagramById(diagramId.data).then((diagram) => {
          diagramStore.loadDiagram(diagram.data)
          loadViewModellingWithDiagram()
        })
      })
    } else {
      diagramStore.loadDiagram(taskDiagram)
      loadViewModellingWithDiagram()
    }
  })
}

const loadViewModellingWithDiagram = () => {
  if (selectedDiagramId.value) {
    showSelectedDiagram(selectedDiagramId.value)
  }

  toolManagementStore.activeTask = task.value
  courseService.getCourse(courseId.value).then((course) => {
    toolManagementStore.activeCourse = course.data
    router.push('/modeling')
  })
}
</script>

<style scoped>
.task {
  width: auto;
  margin: 20px 20px;
}

.task-header-title {
  display: flex;
  align-items: center;
}

.task-main {
  /* grid with left and right side */
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  grid-gap: 40px;
  margin: 20px 10px;
}

.submit-btn {
  /* center in grid */
  justify-self: center;
}

.grid-right {
  /* center */
  display: flex;
  flex-direction: column;
  align-items: center;

  width: 100%;
}

.grid-left {
  text-align: center;
}

.task-trials-caption {
  /* 1 item left 1 item right */
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.modeling-container {
  width: 100%;
  height: 350px;
}

.modelPreview {
  position: relative;
  width: 100%;
  height: 100%;
}

.align-items-center {
  display: flex;
  align-items: center;
}

.margin-right-5px {
  margin-right: 5px;
}

.switchRole {
  display: flex;
  justify-content: center;
  margin: 10px 20px;
}

.hide {
  display: none;
}
</style>
