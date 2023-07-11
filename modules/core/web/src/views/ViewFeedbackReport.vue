<template>
  <BasicBackground>
    <v-card class="card">
      <v-card-title class="title">
        <span>Zur Verbesserung unseres Dienstes würden wir uns über Feedback freuen</span>
      </v-card-title>
      <v-card-text class="text">
        <v-textarea v-model="feedback.text"></v-textarea>
      </v-card-text>
      <v-card-actions>
        <v-spacer>
          <v-btn color="primary-dark" variant="flat" @click="sendFeedback">Abschicken</v-btn>
        </v-spacer>
      </v-card-actions>
    </v-card>
  </BasicBackground>
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Vielen Dank für dein Feedback!</v-snackbar>
</template>

<script setup lang="ts">
import Feedback from "@/model/Feedback";
import User from "../model/User";
import feedbackService from "../services/feedback.service";
import BasicBackground from "@/components/BasicBackground.vue";
import { useAuthUserStore } from "@/stores/authUserStore";
import { ref } from "vue";

const authUserStore = useAuthUserStore();
const feedback = ref<Feedback>({} as Feedback);
const snackbarSuccess = ref(false);

const sendFeedback = () => {
  const user: User = authUserStore.user as User;
  feedback.value.firstName = user.username;
  feedback.value.lastName = user.email;

  feedbackService.createFeedback(feedback.value).then(() => {
    const user: User = authUserStore.user as User;
    feedback.value.firstName = user.username;
    feedback.value.lastName = user.email;

    feedbackService.createFeedback(feedback.value).then(() => {
      snackbarSuccess.value = true;
      feedback.value = {} as Feedback;
    });
  });
};
</script>

<style scoped lang="scss">
.card {
  width: 700px;
  margin: auto;
  padding: 20px;
  text-align: center;
}

.title {
  font-size: 1.1em;
  display: grid;
  grid-gap: 10px;
  grid-template-columns: repeat(1, 1fr);
  align-items: center;
  justify-items: stretch;
  margin-bottom: 15px;
}

.text {
  font-size: large;
  display: grid;
  grid-gap: 20px;
  grid-template-columns: repeat(1, 1fr);
  align-items: center;
  justify-items: stretch;
}
</style>
