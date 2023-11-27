<template>
  <v-dialog v-model="courseDialog" width="70%">
    <v-card>
      <v-card-title>
        <span v-if="newCourse">Neuen Kurs erstellen</span>
        <span v-if="!newCourse">Kurs bearbeiten: {{ course.name }}</span>
      </v-card-title>
      <v-form ref="form" v-model="valid" @submit.prevent>
        <v-card-text>
          <v-row>
            <v-col>
              <v-text-field v-model="course.name" color="primary" variant="underlined" label="Name" :rules="[(v: any) => !!v || 'Item is required']" required></v-text-field>
              <v-text-field v-model="course.description" color="primary" variant="underlined" label="Beschreibung" :rules="[(v: any) => !!v || 'Item is required']" required></v-text-field>
              <v-text-field v-model="course.keyPassword" color="primary" variant="underlined" label="Passwort" :rules="[(v: any) => !!v || 'Item is required']" required></v-text-field>
            </v-col>
            <v-col>
              <v-select v-model="course.semester" color="primary" variant="underlined" label="Semester" :rules="[(v: any) => !!v || 'Item is required']" required :items="semesters" item-title="name" return-object></v-select>
              <v-select v-model="course.location" color="primary" variant="underlined" label="Standort" :items="['Friedberg', 'Gießen']" :rules="[(v: any) => !!v || 'Item is required']" required></v-select>
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions class="card-actions">
   
          <v-btn v-if="!newCourse" color="error" variant="flat" @click="deleteCourse">Kurs löschen</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="error" variant="flat" @click="_cancel"> Abbrechen </v-btn>
          <v-btn v-show="!loading" color="primary" variant="flat" type="submit" @click="_confirm"> Speichern </v-btn>
          <v-progress-circular v-if="loading" color="primary" indeterminate size="40"></v-progress-circular>
        </v-card-actions>
      </v-form>
    </v-card>
    <v-snackbar v-model="snackbarFail" :timeout="2500"> Kurs konnte nicht erstellt werden, bitte versuchen Sie es erneut </v-snackbar>
  </v-dialog>
  <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>
</template>

<script setup lang="ts">
import {watch, ref } from "vue";
import { useAuthUserStore } from "../stores/authUserStore";
import courseService from "../services/course.service";
import CoursePL from "../model/course/CoursePL";
import semesterService from "../services/semester.service";
import Semester from "../model/Semester";
import DialogConfirmVue from "../dialog/DialogConfirm.vue";
import { useRouter } from "vue-router";

const courseDialog = ref<boolean>(false);
const dialogTitle = ref<string>("");

const dialogConfirm = ref<typeof DialogConfirmVue>();
const authUserStore = useAuthUserStore();
const router = useRouter();

const semesters = ref<Semester[]>([]);

// New Course or editing existing course
const newCourse = ref(true);
const course = ref<CoursePL>({} as CoursePL);

const snackbarFail = ref(false);
const loading = ref(false);
// Form
const valid = ref();
const courseSemester = ref("");

const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = (courseId: number | undefined) => {
  loading.value = false; 
  initializeSemesters();
  if (courseId == undefined) {
    newCourse.value = true;
    initializeDialogForNewCourse();
  } else {
    newCourse.value = false;
    initializeDialogForEditingCourse(courseId);
  }
  courseDialog.value = true;

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

//the watcher  is used to reset the form when opening/closing the dialog. 
watch(courseDialog, (newVal) => {
  if (newVal) {
    // Dialog opened, fetch course data if editing
    if (!newCourse.value && course.value.id) {
      initializeDialogForEditingCourse(course.value.id);
    }
  } else {
    // Dialog closed, reset course data
    resetDialog();
  }
});

const resetDialog = () => {
  course.value = {}as CoursePL; 
  valid.value = false; 

};

const initializeDialogForNewCourse = () => {
  dialogTitle.value = "Neuen Kurs erstellen";
};

const initializeDialogForEditingCourse = (courseId: number) => {
  courseService.getCourse(courseId).then((response) => {
    course.value = response.data;
    refreshTextFields();
  });
  dialogTitle.value = "Bearbeiten: ";
};

const refreshTextFields = () => {
  courseSemester.value = course.value.semester.name;
};

// TODO
const _confirm = async () => {
  loading.value = true;
  
  
    if (valid.value) {
      course.value.active = true;
      let userId = authUserStore.auth.user?.id;
      if (userId != undefined) course.value.owner = userId;
      if (newCourse.value == true) {
        courseService
          .postCourse(course.value)
          .then((response) => {
            courseDialog.value = false;
            resolvePromise.value(response.data.id);
          })
          .catch((error) => {
            snackbarFail.value = true;
            console.log(error);
          });
      } else {
        console.log(course);
        courseService
          .putCourse(course.value)
          .then((response) => {
            courseDialog.value = false;
            resolvePromise.value(response.data.id);
          })
          .catch((error) => {
            console.log(error);
          });
      }
    }
    setTimeout(() => {
      loading.value = false;
    }, 1000);
    
  
};


const _cancel = () => {
  courseDialog.value = false;
  resolvePromise.value(undefined);
};

const deleteCourse = () => {
  if (dialogConfirm.value) {
    dialogConfirm.value.openDialog(`Lösche Kurs: ${course.value.name}`, "Willst du den Kurs wirklich löschen?").then((result: boolean) => {
      if (result) {
        courseService
          .deleteCourse(course.value.id)
          .then(() => {
            courseDialog.value = false;
            resolvePromise.value(course.value.id);
            router.push("/course");
          })
          .catch((error) => {
            console.log(error);
          });
      }
    });
  }
};

const initializeSemesters = () => {
  semesterService.getAllSemesters().then((response) => {
    semesters.value = response;
  });
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped>
.card-actions {
  padding: 1rem;
}
</style>
