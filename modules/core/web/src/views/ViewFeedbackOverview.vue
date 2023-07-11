<!-- eslint-disable vue/valid-v-slot -->
<template>
  <DialogConfirmVue ref="dialogConfirm"></DialogConfirmVue>
  <BasicBackground>
    <v-card class="card">
      <v-card-title class="title"> </v-card-title>
      <v-card-text class="text">
        <v-data-table class="dataTable" :headers="headers" :items="feedbacks">
          <template #item.status="{ item }">
            <v-select v-model="item.props.title.status" :hide-details="false" variant="plain" :items="feedbackStatuses" @update:model-value="changeStatus(item.props.title)"></v-select>
          </template>
          <template #item.actions="{ item }">
            <v-btn @click="deleteFeedback(item.props.title)">Delete</v-btn>
          </template>
        </v-data-table>
        <v-data-table class="dataTable" :headers="headers" :items="feedbacks">
          <template #item.status="{ item }">
            <v-select v-model="item.props.title.status" :hide-details="false" variant="plain" :items="feedbackStatuses" @update:model-value="changeStatus(item.props.title)"></v-select>
          </template>
          <template #item.actions="{ item }">
            <v-btn @click="deleteFeedback(item.props.title)">Delete</v-btn>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
  </BasicBackground>
</template>

<script setup lang="ts">
import feedbackService from "../services/feedback.service";
import Feedback from "../model/Feedback";
import { ref, onMounted } from "vue";
import FeedbackStatus from "@/enums/FeedbackStatus";
import DialogConfirmVue from "@/dialog/DialogConfirm.vue";
import BasicBackground from "@/components/BasicBackground.vue";

const dialogConfirm = ref<typeof DialogConfirmVue>();

const feedbacks = ref<Feedback[]>([]);
const feedbackStatuses = ref<FeedbackStatus[]>();

const headers = ref([
  { title: "F-ID", key: "id" },
  { title: "Username", key: "firstName" },
  { title: "Feedback", key: "text" },
  { title: "TimeStamp", key: "timeStamp" },
  { title: "Status", key: "status" },
  { title: "Actions", key: "actions", sortable: false },
]);

onMounted(() => {
  getFeedbacks();

  feedbackService.getStatuses().then((response) => {
    feedbackStatuses.value = response.data;
  });

  feedbackService.getStatuses().then((response) => {
    feedbackStatuses.value = response.data;
  });
});

const getFeedbacks = () => {
  feedbackService.getFeedbacks().then((response) => {
    feedbacks.value = response.data;
  });
};

const changeStatus = (feedback: Feedback) => {
  feedbackService.updateFeedback(feedback).then(() => {
    getFeedbacks();
  });
};

const deleteFeedback = (feedback: Feedback) => {
  if (dialogConfirm.value) {
    dialogConfirm.value.openDialog(`Lösche Feedback mit der ID: ${feedback.id}`, "Willst du das Feedback wirklich löschen?").then((result: boolean) => {
      if (result) {
        feedbackService.deleteFeedback(feedback.id).then(() => {
          getFeedbacks();
        });
      }
    });
  }
};
</script>

<style scoped lang="scss">
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

  max-height: 80vh;
  overflow: auto;
}

.dataTable {
  // font size small
  font-size: 1rem;
}
</style>
