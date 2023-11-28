<template>
  <DialogConfirm ref="dialogConfirm" />
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Diagramm erfolgreich eingereicht </v-snackbar>
  
  <div class="container" >
    <v-card v-if="activeTask != undefined" class="task-floater" elevation="3">
      <v-card-title>Abgabe</v-card-title>
      <v-card-subtitle>
        <span>Aktuelle Aufgabe: {{ activeTask?.name }}</span
        ><br />
        <span>Aus Kurs: {{ activeCourse?.name }}</span
        ><br />
      </v-card-subtitle>
      <v-card-actions>
        <v-row>
          <v-col cols="10">
            <v-btn @click="submitDiagram(true)">Zurück zur Abgabe</v-btn>
          </v-col>
          <v-col cols="10">
 <v-btn v-if="isStudent && !subBtnProgress" @click="triggerfeedback">Prüfen</v-btn>
    <v-progress-circular v-if="subBtnProgress" indeterminate></v-progress-circular>


          </v-col>
        </v-row>
      </v-card-actions>
    </v-card>
    
<AdvancedFeedback v-if="showAdvancedFeedback" :check="checked.valueOf()"  @on-complete="handleFeedbackComplete" />
      
      
    <div class="navigation">
      <v-system-bar color="white" elevation="1" window>
        <span v-if="diagramStore.diagram.name != ''">
          Ausgewähltes Modell:
          <b>{{ diagramStore.diagram.name }}</b>
        </span>

        <span v-if="diagramStore.diagram.name == ''">
          <v-icon icon="mdi-content-save-alert" class="me-1"></v-icon>
          <span> Denk immer daran dein Modell zu speichern</span>
        </span>

        <v-spacer></v-spacer>

        <!-- <span v-if="diagramStore.saved">
          <v-icon icon="mdi-content-save" color="success"></v-icon>
        </span>
        <span v-if="!diagramStore.saved">
          <v-icon icon="mdi-content-save-alert" color="error" class="me-1"></v-icon>
        </span> -->

        <v-spacer></v-spacer>

        <span class="ms-2">{{ currentDateTime }}</span>
      </v-system-bar>
    </div>

    <div class="left-bar">
      <FileExplorer class="file-explorer"></FileExplorer>
      <ToolBox class="tool-box"></ToolBox>
    </div>

    <div class="middle">
      <ModelingTool :key="modelingToolKey" :is-editable="true"></ModelingTool>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted,watch,defineProps } from "vue";
import SubmitPL from "../model/SubmitPL";
import ModelingTool from "@/components/ModelingTool.vue";
import FileExplorer from "@/components/FileExplorer.vue";
import ToolBox from "@/components/modelingTool/ToolBox.vue";
import DialogConfirm from "@/dialog/DialogConfirm.vue";
import { useDiagramStore } from "../stores/diagramStore";
import { storeToRefs } from "pinia";
import { useToolManagementStore } from "@/stores/toolManagementStore";
import { useRoute,useRouter, onBeforeRouteLeave } from "vue-router";
import diagramService from "@/services/diagram.service";
import Diagram from "@/model/diagram/Diagram";
import { useAuthUserStore } from "../stores/authUserStore";
import evaluationService from "@/services/evaluation.service";
import FeedbackLevel from "@/enums/FeedbackLevel";

import Task from "@/model/task/Task";
import taskService from "../services/task.service";
import courseService from "../services/course.service";

import Category from "@/model/diagram/Category";

import AdvancedFeedback from "@/components/modelingTool/AdvancedFeedback.vue";

const authUserStore = useAuthUserStore();
const userId = ref(authUserStore.auth.user?.id!);

const snackbarSuccess = ref(false);

const router = useRouter();
const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const modelingToolKey = storeToRefs(diagramStore).key;
const activeCourse = toolManagementStore.activeCourse;
const activeTask = toolManagementStore.activeTask;
const selectedDiagramId = ref<number>();
  const dialogConfirm = ref<typeof DialogConfirm>();
    const selectedDiagram = ref<Diagram>();
const currentTime = ref<Date>(new Date());
const diagrams = ref<Diagram[]>([] as Diagram[]);
  const subBtnProgress = ref<boolean>(false);


    const submissionCount = ref(0);
    const checked = ref<boolean>(false)
      const task = ref<Task>({} as Task);
        const courseRole = ref("");
      
        const showAdvancedFeedback = ref(false);     

        const isStudent = computed(() => {
  return localStorage.getItem("usertype") === "STUDENT";
});


onMounted(() => {
  
  courseService.getUserRoleInCourse(userId.value!, activeCourse?.id||0).then((role) => {
    courseRole.value = role;
    console.log("courseRole.value: " + courseRole.value); 
  });
  evaluationService.getSubmissionIdsByUserAndTask(userId.value, activeTask?.id || 0).then((response) => {
    
    submissionCount.value = response.data.length;
   console.log("submissionCount.value: " + submissionCount.value);
  
  });
  
});
const triggerfeedback = () => {
  console.log("triggerfeedback entered")
  dialogConfirm.value?.openDialog("Abgabe: ", "Möchten Sie das Diagram wirklich einreichen?", "Einreichen").then((result: boolean) => {
  if (result) {
    console.log("true triggered")
    showAdvancedFeedback.value = true;
    checked.value=true;
subBtnProgress.value = true;
  }
  else{
    showAdvancedFeedback.value = false;
    subBtnProgress.value = false;
  checked.value=false;
  }
  });
  

}



const handleFeedbackComplete = () => {
  console.log('handleFeedbackComplete triggered');
  subBtnProgress.value = false;
  checked.value = false;
  // Handle completion logic here
};

setInterval(() => {
  currentTime.value = new Date();
}, 1000);

onBeforeRouteLeave((to, from, next) => {
  if (diagramStore.saved) {
    next();diagramStore
  } else {
    const message = "Dein Modell wurde noch nicht gespeichert. Willst du die Seite wirklich verlassen?";
    if (window.confirm(message)) {
      next();
    } else {
      next(false);
    }
  }
});

onMounted(() => {
  window.addEventListener("beforeunload", handleBeforeUnload);
});

const handleBeforeUnload = (event: BeforeUnloadEvent) => {
  event.preventDefault();
  event.returnValue = "";
};

// current weekday day date year time seconds in european format
const currentDateTime = computed(() => {
  const date = currentTime.value;
  const weekday = date.toLocaleDateString("de-DE", { weekday: "long" });
  const day = date.toLocaleDateString("de-DE", { day: "2-digit" });
  const month = date.toLocaleDateString("de-DE", { month: "long" });
  const year = date.toLocaleDateString("de-DE", { year: "numeric" });
  const time = date.toLocaleTimeString("de-DE", {
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });
  return `${weekday}, ${day}. ${month} ${year} ${time}`;
});



const submitDiagram = (returntosubmission:boolean) => {
  return new Promise((resolve, reject) => {
    diagramService.putDiagram(diagramStore.diagram)
      .then(() => {
        toolManagementStore.activeCourse = null;
        toolManagementStore.activeTask = null;
        diagramStore.saved = true;
        console.log("diagramStore.saved: " + diagramStore.saved);
        if (returntosubmission) {
          router.push({ name: "ViewTask", params: { courseId: activeCourse?.id, taskId: activeTask?.id } });
        }
        resolve(diagramStore.diagram); // Resolve the promise when the diagram is successfully put
      })
      .catch((error) => {
        alert("Diagramm konnte nicht gespeichert werden");
        reject(error); // Reject the promise if there's an error
      });
  });
};

/** 
const loadSubmissions = () => {
  evaluationService.getSubmissionIdsByUserAndTask(userId.value, activeTask?.id || 0).then((response) => {
    console.log("userId.value, taskId.value: " + userId.value, activeTask?.id);
    
    const submissionIds = response.data;
    console.log("submissionIds: " + submissionIds);
    submissionCount.value = submissionIds.length;
    console.log("submissionCount.value: " + submissionCount.value);
    console.log("taskSubmissionsResultsTabs.value: " + taskSubmissionsResultsTabs.value);
    if (submissionCount.value > 0) taskSubmissionsResultsTabs.value!.load(task.value);
  });
};
*/
</script>

<style scoped lang="scss">
.container {
  position: relative;
  width: 100%;
  height: 100%;
}

.container {
  display: grid;
  grid-template-columns: 200px 1fr;
  grid-template-rows: 0px 1fr;
  grid-template-areas:
    "header header header"
    "left middle right";
}

.navigation {
  grid-area: header;
  // background-color: #c9c6c6;
}

.left-bar {
  grid-area: left;
  // background-color: #b19e9e;
}

.middle {
  grid-area: middle;
  background-color: #564848;
  position: relative;
  width: 100%;
  height: 100%;
}

.right-bar {
  grid-area: right;
  // background-color: #d1aaaa;
}

.task-floater {
  position: absolute;
  top: 20px;
  right: 25px;
  z-index: 10;
}

// .file-explorer {
//   position: absolute;
//   top: 80px;
//   left: 10px;
// }

// .tool-box {
//   position: absolute;
//   top: 260px;
//   left: 10px;
// }
</style>
