<template>
  <v-dialog v-model="courseDialog" width="70%">
    <v-card>
      <v-card-title>
        <span v-if="newCourse" class="text-h5">Neuen Kurs erstellen</span>
        <span v-if="!newCourse" class="text-h5">Kurs bearbeiten: </span>
      </v-card-title>
      <v-form ref="form" v-model="valid" @submit.prevent>
        <v-card-text>
          <v-row>
            <v-col>
              <v-text-field v-model="courseName" color="#81ba24" variant="underlined" label="Name" :rules="[(v) => !!v || 'Item is required']" required></v-text-field>
              <v-text-field v-model="courseDescription" color="#81ba24" variant="underlined" label="Beschreibung" :rules="[(v) => !!v || 'Item is required']" required></v-text-field>
              <v-text-field v-model="coursePassword" color="#81ba24" variant="underlined" label="Passwort" :rules="[(v) => !!v || 'Item is required']" required></v-text-field>
            </v-col>
            <v-col>
              <v-select v-model="courseSemester" color="#81ba24" variant="underlined" label="Semester" :rules="[(v) => !!v || 'Item is required']" required :items="semesterLabels"></v-select>
              <v-select v-model="courseLocation" color="#81ba24" variant="underlined" label="Standort" :items="['Friedberg', 'Gießen']" :rules="[(v) => !!v || 'Item is required']" required></v-select>
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-btn class="btn-red">Kurs löschen</v-btn>
          <v-spacer></v-spacer>
          <v-btn class="btn-red" @click="_cancel"> Abbrechen </v-btn>
          <v-btn id="btn-confirm" type="submit" @click="_confirm"> Speichern </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
    <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Kurs erfolgreich erstellt </v-snackbar>
    <v-snackbar v-model="snackbarFail" :timeout="2500"> Kurs konnte nicht erstellt werden, bitte versuchen Sie es erneut </v-snackbar>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useAuthUserStore } from "../stores/authUserStore";
import courseService from "../services/course.service";
import CoursePL from "../model/course/CoursePL";
import semesterService from "../services/semester.service";
import Semester from "../model/Semester";

const courseDialog = ref<boolean>(false);
const dialogTitle = ref<string>("");

const authUserStore = useAuthUserStore();

const semesters = new Map<string, Semester>([]);
const semesterLabels = ref<string[]>([]);

// New Course or editing existing course
const newCourse = ref(true);
const course = ref<CoursePL>({} as CoursePL);

const snackbarSuccess = ref(false);
const snackbarFail = ref(false);

// Form
const valid = ref();
const courseName = ref("");
const courseSemester = ref("");
const courseDescription = ref("");
const coursePassword = ref("");
const courseLocation = ref("");

const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = (courseId: number | undefined) => {
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

const initializeDialogForNewCourse = () => {
  dialogTitle.value = "Neuen Kurs erstellen";
};

const initializeDialogForEditingCourse = (courseId: number) => {
  courseService.getCourse(courseId).then((response) => {
    course.value = response.data;
  });
  dialogTitle.value = "Bearbeiten: ";
};

// TODO
const _confirm = () => {
  if (valid.value) {
    course.value.name = courseName.value;
    course.value.description = courseDescription.value;
    course.value.keyPassword = coursePassword.value;
    course.value.active = true;
    let date = new Date();
    let year = date.getFullYear();
    let day = date.getDate();
    let month = date.getMonth() + 1;
    course.value.creationDate = day + "-" + month + "-" + year;
    course.value.location = courseLocation.value;
    let userId = authUserStore.auth.user?.id;
    if (userId != undefined) course.value.owner = userId;
    let semesterId = semesters.get(courseSemester.value)?.id;
    if (semesterId != undefined) course.value.semesterId = semesterId;

    courseService
      .postCourse(course.value)
      .then((response) => {
        snackbarSuccess.value = true;
        courseDialog.value = false;
        resolvePromise.value(response.data.id);
      })
      .catch((error) => {
        snackbarFail.value = true;
        console.log(error);
      });
  }
};

const _cancel = () => {
  courseDialog.value = false;
  resolvePromise.value(undefined);
};

const initializeSemesters = () => {
  semesterService.getAllSemesters().then((response) => {
    response.forEach((semester) => {
      semesterLabels.value.push(semester.name);
      semesters.set(semester.name, semester);
    });
  });
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped>
#btn-confirm {
  background-color: #81ba24;
  color: #ffffff;
}

.btn-red {
  background-color: #db3e1f;
  color: #ffffff;
}
</style>
