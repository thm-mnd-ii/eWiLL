<template>
  <v-breadcrumbs class="breadcrumbs" bg-color="primary-dark" :items="items"></v-breadcrumbs>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { ref } from "vue";

import courseService from "../services/course.service";
import taskService from "../services/task.service";

const items = ref<string[]>(["Start"]);

//define props
const props = defineProps<{
  link: string;
}>();

// define word replacement
const wordReplacement = (word: string) => {
  switch (word) {
    case "course":
      return "Kurs";
    case "task":
      return "Aufgabe";
    default:
      return word;
  }
};

onMounted(() => {
  calculateBreadCrumb();
});

const calculateBreadCrumb = async () => {
  // add items to breadcrumb
  const path = props.link.split("/").filter((item) => item != "");

  for (let i = 0; i < path.length; i++) {
    const item = path[i];
    const word = wordReplacement(item);

    // if last item was course, add course name
    if (i > 0 && path[i - 1] == "course") {
      let courseName = courseService.getCourse(Number(path[i])).then((response) => {
        return response.data.name;
      });

      items.value.push(await courseName);
    } else if (i > 0 && path[i - 1] == "task") {
      let taskName = taskService.getTask(Number(path[i])).then((response) => {
        return response.name;
      });

      items.value.push(await taskName);
    } else {
      items.value.push(word);
    }
  }
};

// breadcrumb items
</script>

<style scoped lang="scss">
.breadcrumbs {
  height: 60px;
  margin-bottom: 20px;
}
</style>
