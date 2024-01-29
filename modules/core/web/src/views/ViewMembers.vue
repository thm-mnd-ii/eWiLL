<!-- eslint-disable vue/valid-v-slot -->
<template>
  <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>
  <div class="task">
    <v-card>
      <v-card-title class="task-header-title">
        <h3 class="headline mb-0">Teilnehmer</h3>
        <v-spacer></v-spacer>
        <v-btn variant="text" icon="mdi-keyboard-return" color="dark-gray" @click="returnToCourse"></v-btn>
      </v-card-title>
      <v-card-text>
        <p>{{ courseName }}</p>
        <br />
        <div class="align-items-center">
          <v-chip prepend-icon="mdi-account-circle" color="secondary" text-color="white" label> {{ courseRole }} </v-chip>
          <v-spacer></v-spacer>
          <v-btn v-if="courseRole == 'OWNER' || courseRole == 'TUTOR'" variant="text" color="red" @click="kickAllStudents">Alle Studenten entfernen</v-btn>
        </div>
      </v-card-text>
    </v-card>

    <div class="container">
      <v-text-field v-model="search" label="Search" density="compact" prepend-icon="mdi-magnify" variant="underlined" hide-details class="search-field"></v-text-field>
      <v-data-table :headers="headers" :items="members" item-value="name" class="elevation-1" :search="search" density="default" height="480px">
        <template #item.role="{ item }">
          <v-select v-model="item.role" item variant="plain" :items="['STUDENT', 'TUTOR', 'OWNER']" class="select" :disabled="courseRole == 'STUDENT'" @update:model-value="changeRole(item)"></v-select>
        </template>
        <template #item.actions="{ item }">
          <v-btn icon="mdi-delete" color="red" variant="text" :disabled="courseRole == 'STUDENT'" @click="kickUser(item.user)"></v-btn>
        </template>
      </v-data-table>
    </div>

    <div class="task-main"></div>
  </div>
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Rolle erfolgreich geändert </v-snackbar>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import courseService from "../services/course.service";
import CoursePL from "../model/course/CoursePL";
import Member from "../model/course/Member";
import DialogConfirmVue from "../dialog/DialogConfirm.vue";

const route = useRoute();
const router = useRouter();
const authUserStore = useAuthUserStore();
const dialogConfirm = ref<typeof DialogConfirmVue>();
const snackbarSuccess = ref(false);

const members = ref<Member[]>();

const course = ref<CoursePL>();
const courseName = ref("");
const courseId = ref(Number(route.params.courseId));
const userId = ref(authUserStore.auth.user?.id!);
const courseRole = ref("");

const search = ref("");
const headers = [
  { title: "Name", align: "start", key: "user.lastName" },
  { title: "Vorname", align: "start", key: "user.firstName" },
  { title: "E-Mail", align: "start", key: "user.email" },
  { title: "Rolle", align: "start", key: "role" },
  { title: "Aktion", align: "start", key: "actions", sortable: false },
];

onMounted(() => {
  courseService.getUserRoleInCourse(userId.value, courseId.value).then((response) => {
    if (response == "NONE") {
      router.push("/course/" + route.params.courseId + "/signup");
    } else {
      courseRole.value = response;
      loadCourse();
      loadCourseMembers();
    }
  });
});

const loadCourse = () => {
  courseService.getCourse(courseId.value).then((response) => {
    course.value = response.data;
    courseName.value = course.value.name;
  });
};

const loadCourseMembers = () => {
  courseService.getCourseMembers(courseId.value).then((response) => {
    members.value = response.data;
  });
};

const kickUser = (user: any) => {
  console.log(user);
  if (dialogConfirm.value) {
    dialogConfirm.value.openDialog(`Entferne Nutzer: ${user.firstName} ${user.lastName}`, "Wollen Sie den Nutzer wirklich aus dem Kurs entfernen?", "Entfernen").then((result: boolean) => {
      if (result) {
        courseService
          .leaveCourse(courseId.value, user.id)
          .then(() => {
            loadCourseMembers();
          })
          .catch((error) => {
            console.log(error);
          });
      }
    });
  }
};

const kickAllStudents = () => {
  if (dialogConfirm.value) {
    dialogConfirm.value.openDialog(`Entferne alle Studenten`, "Wollen Sie wirklich alle Stundenten aus dem Kurs entfernen?", "Entfernen").then((result: boolean) => {
      if (result) {
        members.value?.forEach((student) => {
          if (student.role == "STUDENT") {
            courseService.leaveCourse(courseId.value, student.user.id);
          }
        });
      }
      loadCourseMembers();
    });
  }
};

const changeRole = (member: Member) => {
  courseService
    .changeUserRole(courseId.value, member.user.id, member.role, userId.value)
    .then(() => {
      snackbarSuccess.value = true;
      loadCourseMembers();
    })
    .catch(() => {
      loadCourseMembers();
    });
};

const returnToCourse = () => {
  router.back();
};
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

.align-items-center {
  display: flex;
  align-items: center;
}

.container {
  margin-top: 30px;
}

.search-field {
  margin-bottom: 20px;
}

.select {
  width: 90px;
}
</style>
