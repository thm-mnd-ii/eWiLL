<template>
  <v-dialog v-model="createCategoryDialog" activator="parent" width="500px">
    <v-card>
      <v-card-title>
        <span class="text-h5">Create Category</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-text-field v-model="createCategoryName" label="Name*" required></v-text-field>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="createCategoryDialog = false"> Close </v-btn>
        <v-btn variant="text" @click="createCategoryClick"> Erstellen </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { defineProps, ref, defineEmits, watch, computed, onMounted } from "vue";

// define props with ts
const props = defineProps<{
  openDialog: boolean;
}>();

// define emit with ts
const emit = defineEmits<{
  (event: "closeDialog"): void;
}>();

const createCategoryDialog = ref<boolean>(props.openDialog);

// watch props for changes
watch(
  () => props.openDialog,
  (value) => {
    createCategoryDialog.value = value;
    console.log(value);
  }
);

const closeDialog = () => {
  createCategoryDialog.value = false;
  emit("closeDialog");
};

// Constants for category dialog
// const createCategoryDialog = ref(false);
// const createCategoryName = ref("");

// const createCategoryClick = () => {
//   diagramService
//     .postCategory(createCategoryName.value, userId.value)
//     .then((response) => {
//       if (response.status == 200) {
//         categoryNames.value.push(createCategoryName.value);
//         createCategoryDialog.value = false;
//         reload();
//       }
//     })
//     .catch((error) => {
//       console.log(error);
//       alert(ALERT_UPS);
//     });
// };

// const finalDeleteClick = () => {
//   if (deleteItemDiagram.value != null) {
//     diagramService
//       .deleteDiagram(deleteItemDiagram.value)
//       .then(() => {
//         reload();
//         deleteDialog.value = false;
//       })
//       .catch(() => {
//         alert(ALERT_UPS);
//       });
//   } else if (deleteItemCategory.value != null) {
//     const categoryToDelete = searchForCategory(deleteItemCategory.value);
//     if (categoryToDelete.id == null) {
//       alert(ALERT_UPS);
//     } else {
//       diagramService
//         .deleteCategory(categoryToDelete)
//         .then(() => {
//           reload();
//           deleteDialog.value = false;
//         })
//         .catch(() => {
//           alert(ALERT_UPS);
//         });
//     }
//   }
// };

// const searchForCategory = (categoryName: string): Category => {
//   const tmpCategory = {} as Category;
//   categories.forEach((category) => {
//     if (category.name == categoryName) {
//       tmpCategory.id = category.id;
//       tmpCategory.name = category.name;
//       tmpCategory.userId = category.userId;
//       return tmpCategory;
//     }
//   });
//   return tmpCategory;
// };
</script>

<style scoped></style>
