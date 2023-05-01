<template>
  <v-parallax class="background" src="https://images.unsplash.com/photo-1617957718614-8c23f060c2d0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1332&q=80">
    <v-card class="card">
      <v-card-title class="title"> </v-card-title>
      <v-card-text class="text">
        <v-table>
          <thead>
            <tr>
              <th class="text-left">ID</th>
              <th class="text-left">Feedback</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="feedback in feedbacks" :key="feedback.id">
              <td class="text-left">{{ feedback.id }}</td>
              <td class="text-left">{{ feedback.text }}</td>
            </tr>
          </tbody>
        </v-table>
      </v-card-text>
    </v-card>
  </v-parallax>
</template>

<script setup lang="ts">
import feedbackService from "../services/feedback.service";
import Feedback from "../model/Feedback";
import { ref, onMounted } from "vue";

const feedbacks = ref<Feedback[]>([]);

onMounted(() => {
  getFeedbacks();
});

const getFeedbacks = () => {
  feedbackService.getFeedbacks().then((response) => {
    feedbacks.value = response.data;
    console.log(feedbacks.value);
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
  max-width: 800px;
  overflow-x: auto;
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

  max-height: 60vh;
  overflow: auto;
}

.btn {
  background-color: #ff8f45;
}
</style>
