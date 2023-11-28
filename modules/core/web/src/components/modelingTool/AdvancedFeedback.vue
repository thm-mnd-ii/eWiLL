<template>

    


<TaskSubmissionsResultsTabs v-if="finished"   ref="taskSubmissionsResultsTabs"></TaskSubmissionsResultsTabs>

</template>

<script setup lang="ts">
import { ref, computed, onMounted,defineEmits   } from "vue";
import SubmitPL from "../../model/SubmitPL";


import { useDiagramStore } from "@/stores/diagramStore";
import { storeToRefs } from "pinia";
import { useToolManagementStore } from "@/stores/toolManagementStore";
import { useRoute,useRouter } from "vue-router";
import diagramService from "@/services/diagram.service";
import Diagram from "@/model/diagram/Diagram";
import { useAuthUserStore } from "@/stores/authUserStore";
import evaluationService from "@/services/evaluation.service";
import FeedbackLevel from "@/enums/FeedbackLevel";
import TaskSubmissionsResultsTabs from "@/components/TaskSubmissionsResultsTabs.vue";
import Task from "@/model/task/Task";
import taskService from "@/services/task.service";

import Category from "@/model/diagram/Category";
import CourseRoles from "@/enums/CourseRoles";
import submissionService from "@/services/submission.service";


const authUserStore = useAuthUserStore();
const userId = ref(authUserStore.auth.user?.id!);

const route = useRoute();


const diagramStore = useDiagramStore();
const toolManagementStore = useToolManagementStore();

const activeCourse = toolManagementStore.activeCourse;
const activeTask = toolManagementStore.activeTask;
const selectedDiagramId = ref<number>();

const selectedDiagram = ref<Diagram>();
const currentTime = ref<Date>(new Date());
const diagrams = ref<Diagram[]>([] as Diagram[]);
const subBtnProgress = ref<boolean>(false);
const submissionCount = ref(0);
const taskSubmissionsResultsTabs = ref<typeof TaskSubmissionsResultsTabs>();
const task = ref<Task>({} as Task);
const activeTaskId = ref<number>();
const finished = ref<boolean>(false);
const props = defineProps({
  active: Boolean,
  isVisible: Boolean,
  check: Boolean,
});

const emits = defineEmits(["onComplete"]);
onMounted(async () => {
    
    activeTaskId.value=activeTask?.id || 0;
    console.log("activeTaskId.value in onmount: " + activeTaskId.value);
    
    await  loadTask();
    await loadSubmissions();


 
    


    

  await   saveandcheckdiag();
     console.log("emit reached in advancedfeedback");


 console.log("emits passed in advancedfeedback");
});

// Fetch submissions data




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

function removeWeekday(dateTimeStr:any) {
  return dateTimeStr.split(', ').slice(1).join(', ');
}

// Function to convert dueDate string into a Date object
function convertDueDate(dueDateStr:any) {
  const [day, month, year, time] = dueDateStr.split(/\.|\s/);
  return new Date(`${year}-${month}-${day} ${time}`);
}


const saveandcheckdiag = async () => {
  
  try {
    await submitDiagram().then((response) => {
      const val= response as Diagram;
      selectedDiagramId.value = val.id;
      selectedDiagram.value = val;
      console.log("saveandcheckdiag method called", selectedDiagramId.value);
      console.log("response: " + JSON.stringify(response));

    })
 await  checkdiagramm();
  } catch (error) {
    console.error("Error in submitDiagram:", error);
    // Handle any errors that occurred in submitDiagram
  }

};

const submitDiagram = () => {
  return new Promise((resolve, reject) => {
    diagramService.putDiagram(diagramStore.diagram)
      .then(() => {
        toolManagementStore.activeCourse = null;
        toolManagementStore.activeTask = null;
        diagramStore.saved = true;
        console.log("diagramStore.saved: " + diagramStore.saved);
        resolve(diagramStore.diagram); // Resolve the promise when the diagram is successfully put
      })
      .catch((error) => {
        alert("Diagramm konnte nicht gespeichert werden");
        reject(error); // Reject the promise if there's an error
      });
  });
};

const loadTask =  () => {
  taskService.getTask(activeTask?.id || 0).then((response) => {
    task.value = response;
    
  });
};

const loadSubmissions =   (selectedTaskIndex?: Number) => {
    
    evaluationService.getSubmissionIdsByUserAndTask(userId.value, activeTask?.id || 0 ).then((response) => {
    const submissionIds = response.data;
    submissionCount.value = submissionIds.length;
    console.log("submissionCount.value: " + submissionCount.value)
    console.log("taskId.value: in loadsubmissions " + activeTask?.id || 0);
    console.log("the task value inside loadsubmissions: " + task.value);
    taskSubmissionsResultsTabs.value?.load(task.value, selectedTaskIndex);
    console.log("end reached loadSubmissions");
 
});
};
const waitUntilSubmissionIsEvaluated = (submissionId: number) => {
  return new Promise((resolve) => {
    const interval = setInterval(() => {
      evaluationService.getSubmissionById(submissionId, FeedbackLevel.NOTHING).then((response) => {
        if (response.status == 200) {
          clearInterval(interval);
          resolve(response.data);
       console.log("waitUntilSubmissionIsEvaluated reached :",response.data);
        }
      });
    }, 1000);
  });
};

const checkdiagramm = () => {
  loadTask();
  const date = (activeTask?.dueDate) as string;
  const currentDateTimeObj = new Date(removeWeekday(currentDateTime.value));
const dueDateObj = convertDueDate(date);

  console.log("checkdiagramm method called", selectedDiagramId.value);
  if (selectedDiagramId.value == undefined) {
   console.log("diagramm is not saved yet")
  } else {
  
    if (activeTask?.maxSubmissions as number > submissionCount.value && ( currentDateTimeObj < dueDateObj ) ) {
      console.log("currenttime ",new Date(currentTime.value));
      console.log("Condition Result:", (activeTask?.dueDate));

   console.log("props value:", props.check.valueOf());
      if (props.check == true) {
        
    console.log("diagramm is saved");
        const submitPL = {} as SubmitPL;
        submitPL.diagramId = selectedDiagramId.value!;
        console.log("selectedDiagramId: " + selectedDiagramId.value);
        submitPL.taskId = activeTask?.id || 0;
        console.log("taskId in checkdiagramm: " + activeTask?.id || 0);
        submitPL.userId = userId.value;
        console.log("userId: " + userId.value);
        evaluationService.submitDiagram(submitPL).then((submissionId) => {
          subBtnProgress.value = true;
          waitUntilSubmissionIsEvaluated(submissionId.data).then(() => {
            subBtnProgress.value = false;
        
            loadSubmissions();
          });
        });
      
    console.log("reached end");
      }
 
  }
  else{
    alert("Maximale Anzahl an Abgaben erreicht");
  }
}
emits('onComplete');
finished.value=true;
};

</script>