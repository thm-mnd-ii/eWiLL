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
import { useRouter } from "vue-router";
import courseService from "../services/course.service";
import CoursePL from "../model/course/CoursePL";

const courseDialog = ref<boolean>(false);
const dialogTitle = ref<string>("");
const deleteMessage = ref<string | undefined>(undefined);

const router = useRouter();
const authUserStore = useAuthUserStore();

const semesterLabels = ref<string[]>();

// New Course or editing existing course
const newCourse = ref(true);

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
  if (courseId != undefined) newCourse.value = false;

  semesterLabels.value = generateSemesterLables();
  if (courseId == undefined) initializeDialogForNewCourse();
  else initializeDialogForEditingCourse(courseId);
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
  dialogTitle.value = "Neuen Kurs erstellen";
};

// TODO
const _confirm = () => {
  if (valid.value) {
    const course = {} as CoursePL;
    course.name = courseName.value;
    course.description = courseDescription.value;
    course.keyPassword = coursePassword.value;
    course.active = true;
    let date = new Date();
    let year = date.getFullYear();
    let day = date.getDate();
    let month = date.getMonth() + 1;
    course.creationDate = day + "-" + month + "-" + year;
    course.location = courseLocation.value;
    let userId = authUserStore.auth.user?.id;
    if (userId != undefined) course.owner = userId;
    course.semesterId = 0;

    courseService
      .postCourse(course)
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
  resolvePromise.value(false);
};

const generateSemesterLables = () => {
  const semesterLabels: string[] = [];
  let year = new Date().getFullYear();
  // Last 2 digits
  year = Number(String(year).slice(-2));

  for (let i = year - 1; i <= year + 1; i++) {
    semesterLabels.push("SS" + i);
    semesterLabels.push("WS" + i);
  }
  return semesterLabels;
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
