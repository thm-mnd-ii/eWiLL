<template>
  <v-parallax class="background" src="https://images.unsplash.com/photo-1617957718614-8c23f060c2d0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1332&q=80">
    <v-card class="card">
      <v-card-title class="title"> </v-card-title>
      <v-card-text class="text">
        <v-table>
          <thead>
            <tr>
              <th class="text-left">F-ID</th>
              <th class="text-left">Username</th>
              <th class="text-left">Feedback</th>
              <th class="text-left">TimeStamp</th>
              <th class="text-left">Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="feedback in feedbacks" :key="feedback.id">
              <td class="text-left">{{ feedback.id }}</td>
              <td class="text-left">{{ feedback.firstName }}</td>
              <td class="text-left">{{ feedback.text }}</td>
              <td class="text-left">{{ feedback.timeStamp }}</td>
              <td class="text-left">
                <v-select v-model="feedback.status" :hide-details="true" variant="underlined" :items="feedbackStatuses" @update:model-value="changeStatus(feedback)"></v-select>
              </td>
              <td class="text-left">
                <v-btn @click="deleteFeedback(feedback)">Delete</v-btn>
              </td>
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
import FeedbackStatus from "@/enums/FeedbackStatus";

const feedbacks = ref<Feedback[]>([]);
const feedbackStatuses = ref<FeedbackStatus[]>();

onMounted(() => {
  getFeedbacks();

  feedbackService.getStatuses().then((response) => {
    feedbackStatuses.value = response.data;
  });
});

const getFeedbacks = () => {
  feedbackService.getFeedbacks().then((response) => {
    feedbacks.value = response.data;
    console.log(feedbacks.value);
  });
};

const changeStatus = (feedback: Feedback) => {
  feedbackService.updateFeedback(feedback).then((response) => {
    console.log(response.data);
  });
};

const deleteFeedback = (feedback: Feedback) => {
  feedbackService.deleteFeedback(feedback.id).then((response) => {
    console.log(response.data);
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
  width: 90vw;
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
