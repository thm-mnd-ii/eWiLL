<template>
  <v-dialog v-model="createCategoryDialog" width="500px">
    <v-card>
      <v-card-title>
        <span class="text-h5">Create Category</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-text-field v-model="newCategoryName" label="Name*" required></v-text-field>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="closeDialog"> Close </v-btn>
        <v-btn variant="text" @click="createCategory"> Erstellen </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { defineProps, ref, defineEmits, watch } from "vue";
import diagramService from "../services/diagram.service";
import { useAuthUserStore } from "../stores/authUserStore";

const authUserStore = useAuthUserStore();

// define props with ts
const props = defineProps<{
  openDialog: boolean;
}>();

// define emit with ts
const emit = defineEmits<{
  (event: "closeDialog"): void;
}>();

// watch props for changes
watch(
  () => props.openDialog,
  (value) => {
    createCategoryDialog.value = value;
    console.log(value);
  }
);

const createCategoryDialog = ref<boolean>(props.openDialog);
const newCategoryName = ref<string>("");

const closeDialog = () => {
  createCategoryDialog.value = false;
  emit("closeDialog");
};

const createCategory = () => {
  let userId = authUserStore.auth.user?.id;
  if (userId === undefined) {
    console.log("userId is undefined");
    return;
  }

  diagramService.postCategory(newCategoryName.value, userId)
  .then(() => {
    closeDialog();
  })
  .catch((error) => {
    console.log(error);
    alert("Kategorie konnte nicht gespeichert werden");
  });
};
</script>

<style scoped></style>
