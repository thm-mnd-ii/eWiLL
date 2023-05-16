<template>
  <div class="container">
    <v-card class="card">
      <v-card-item>
        <v-card-title>Für den Kurs {{ course?.name }} einschreiben</v-card-title>

        <v-card-subtitle>
          Verantwortlich: Not yet implemented <br />
          Semester: Not yet implemented <br />
          Standort: {{ course?.location }}
        </v-card-subtitle>
      </v-card-item>

      <v-text-field v-model="key" label="Einschreibeschlüssel" class="textfield" variant="solo"></v-text-field>
      <v-card-actions>
        <v-btn class="button" @click="signup">Einschreiben</v-btn>
      </v-card-actions>
    </v-card>
  </div>
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Einschreibung erfolgreich </v-snackbar>
  <v-snackbar v-model="snackbarPassword" :timeout="2500"> Passwort falsch, bitte versuchen Sie es erneut </v-snackbar>
  <v-snackbar v-model="snackbarError" :timeout="2500"> Oops, something went wrong </v-snackbar>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import CoursePL from "../model/course/CoursePL";
import courseService from "../services/course.service";

const route = useRoute();
const router = useRouter();
const authUserStore = useAuthUserStore();
const course = ref<CoursePL>();
const key = ref("");
const courseId = ref(Number(route.params.id));
const userId = ref(authUserStore.auth.user?.id);
const snackbarSuccess = ref(false);
const snackbarPassword = ref(false);
const snackbarError = ref(false);

onMounted(() => {
  courseService
    .getCourse(courseId.value)
    .then((response) => {
      course.value = response.data;
    })
    .catch((error) => {
      console.log(error);
    });
});

const signup = () => {
  if (userId.value != undefined) {
    courseService
      .joinCourse(courseId.value, key.value, userId.value)
      .then((response) => {
        if (response.status == 200) {
          router.push("/course/" + courseId.value);
        }
      })
      .catch((error) => {
        if (error.response.status == 403) {
          snackbarPassword.value = true;
        } else {
          snackbarError.value = true;
          console.log(error);
        }
      });
  }
};
</script>

<style scoped lang="scss">
.card {
  max-width: 600px;
  background-color: #ffcc00;
}

.button {
  background-color: #ffffff;
  margin-left: 36%;
}

.textfield {
  margin-left: 40px;
  margin-right: 40px;
}

.container {
  position: relative;
  left: 32%;
  top: 30%;
}
</style>
