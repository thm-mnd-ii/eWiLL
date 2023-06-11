<!-- eslint-disable vue/valid-v-slot -->
<template>
  <div>
    <v-chip v-if="timeDifference <= 0" prepend-icon="mdi-clock-time-five-outline" color="red">{{ dueDate }}</v-chip>
    <v-chip v-if="timeDifference > 0 && timeDifference <= 7" prepend-icon="mdi-clock-time-five-outline" color="yellow">{{ dueDate }}</v-chip>
    <v-chip v-if="timeDifference > 7" prepend-icon="mdi-clock-time-five-outline" color="green">{{ dueDate }}</v-chip>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { onMounted } from "vue";

const props = defineProps({ dueDateProp: String });

const dueDate = ref();
const timeDifference = ref();

onMounted(() => {
  if (props.dueDateProp != undefined) setDueDate(props.dueDateProp);
});

watch(
  () => props.dueDateProp,
  (first, second) => {
    setDueDate(first!);
  }
);

const setDueDate = (dateTime: string) => {
  dueDate.value = dateTime;
  let day = getDay(dateTime);
  let month = getMonth(dateTime);
  let year = getYear(dateTime);
  let hour = getHour(dateTime);
  let minutes = getMinutes(dateTime);

  let date = new Date(year + "-" + month + "-" + day + "T" + hour + ":" + minutes + ":00");
  let currentDate = new Date();

  timeDifference.value = (date.getTime() - currentDate.getTime()) / (1000 * 3600 * 24);
};

const getDay = (dateTime: string) => {
  return dateTime.slice(0, 2);
};

const getMonth = (dateTime: string) => {
  return dateTime.slice(3, 5);
};

const getYear = (dateTime: string) => {
  return dateTime.slice(6, 10);
};

const getHour = (dateTime: string) => {
  return dateTime.slice(11, 13);
};

const getMinutes = (dateTime: string) => {
  return dateTime.slice(14, 16);
};

defineExpose({ setDueDate });
</script>

<style scoped></style>
