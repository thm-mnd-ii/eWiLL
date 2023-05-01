<template>
  <v-parallax class="background" src="https://images.unsplash.com/photo-1617957718614-8c23f060c2d0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1332&q=80">
    <v-card class="card">
      <v-card-title class="title">
        <span>Zur Verbesserung unseres Dienstes würden wir uns über Feedback freuen</span>
      </v-card-title>
      <v-card-text class="text">
        <v-textarea v-model="feedback"></v-textarea>
      </v-card-text>
      <v-card-actions>
        <v-spacer>
          <v-btn class="btn" @click="sendFeedback">Abschicken</v-btn>
        </v-spacer>
      </v-card-actions>
    </v-card>
  </v-parallax>
  <v-snackbar v-model="snackbarSuccess" :timeout="2500"> Vielen Dank für dein Feedback!</v-snackbar>
</template>

<script setup lang="ts">
import feedbackService from "../services/feedback.service";
import { ref } from "vue";

const feedback = ref("");
const snackbarSuccess = ref(false);

const sendFeedback = () => {
  feedbackService.postFeedback(feedback.value).then(() => {
    snackbarSuccess.value = true;
    feedback.value = "";
  });
};
</script>

<style scoped lang="scss">
.background {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
}

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

.btn {
  background-color: #ff8f45;
}
</style>
