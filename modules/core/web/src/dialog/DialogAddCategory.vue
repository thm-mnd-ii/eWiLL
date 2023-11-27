<template>
  <v-dialog v-model="createCategoryDialog" width="500px">
    <v-card>
      <v-card-title>
        <span class="text-h5">Neuen Ordner erstellen</span>
      </v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="valid">

          <v-text-field v-model="newCategoryName" label="Name*" :rules="[(v: any) => !!v || 'Item is required']" required></v-text-field>

        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="_close"> Close </v-btn>
        <v-btn variant="text" @click="createCategory"> Erstellen </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import diagramService from "../services/diagram.service";
import { useAuthUserStore } from "../stores/authUserStore";

const authUserStore = useAuthUserStore();
const createCategoryDialog = ref(false);
const newCategoryName = ref<string>("");

const form = ref<any>();
const valid = ref(false);

// Promis
const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = () => {
  createCategoryDialog.value = true;

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _confirm = () => {
  createCategoryDialog.value = false;
  resolvePromise.value(true);
};

const _close = () => {
  createCategoryDialog.value = false;
  resolvePromise.value(false);
};

const createCategory = () => {
  let userId = authUserStore.auth.user?.id;
  if (userId === undefined) {
    console.log("userId is undefined");
    return;
  }

  //validate form
  form.value.validate().then(() => {
    if (valid.value) {
      diagramService
        .postCategory(newCategoryName.value, authUserStore.auth.user?.id as number)
        .then(() => {
          newCategoryName.value = "";
          _confirm();
        })
        .catch((error) => {
          console.log(error);
          alert("Ordner konnte nicht gespeichert werden");
        });
    }
  });
};

defineExpose({
  openDialog,
});
</script>

<style scoped></style>
