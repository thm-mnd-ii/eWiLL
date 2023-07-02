<template>
  <v-breadcrumbs class="breadcrumbs" bg-color="primary-dark" :items="items"></v-breadcrumbs>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { ref } from "vue";

import courseService from "../services/course.service";
import taskService from "../services/task.service";
import { watch } from "vue";

const items = ref<Array<{ title: string; disabled: boolean; href: string }>>([
  {
    title: "Home",
    disabled: false,
    href: "/",
  },
]);

//define props
const props = defineProps<{
  link: string;
}>();

watch(
  () => props.link,
  () => {
    items.value = [
      {
        title: "Home",
        disabled: false,
        href: "/",
      },
    ];
    calculateBreadCrumb();
  }
);

// define word replacement
const wordReplacement = (word: string) => {
  switch (word) {
    case "course":
      return "Alle Kurse";
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

  let breadcrumbItem: { title: string; disabled: boolean; href: string };

  for (let i = 0; i < path.length; i++) {
    const item = path[i];
    const title = wordReplacement(item);
    // create relative link for item
    const link = "/" + path.slice(0, i + 1).join("/");
    const disabled = i == path.length - 1;

    breadcrumbItem = {
      title: title,
      disabled: disabled,
      href: link,
    };

    // if last item was course, replace id with course name
    if (i > 0 && path[i - 1] == "course") {
      let newTitle = await courseService.getCourse(Number(path[i])).then((response) => {
        return response.data.name;
      });

      breadcrumbItem.title = newTitle;

      items.value.push(breadcrumbItem);
    }
    // if last item was course, replace id with task name
    else if (i > 0 && path[i - 1] == "task") {
      let newTitle = await taskService.getTask(Number(path[i])).then((response) => {
        return response.name;
      });

      breadcrumbItem.title = newTitle;

      items.value.push(breadcrumbItem);
    } else if (path[i] == "task") {
      continue;
    } else {
      items.value.push(breadcrumbItem);
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
