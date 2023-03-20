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
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import CoursePL from "../model/course/CoursePL";
import courseService from "../services/course.service";

const route = useRoute();
const authUserStore = useAuthUserStore();
const course = ref<CoursePL>();
const key = ref("");
const courseId = ref(Number(route.params.id));
const userId = ref(authUserStore.auth.user?.id);

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
    courseService.joinCourse(courseId.value, key.value, userId.value).then((response) => {
      console.log(response);
      if (response.status == 200) {
        console.log("ok");
      } else if (response.status == 403) {
        console.log("passwort falsch");
      } else {
        console.log("error");
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
