<template>
  <DialogAddCategoryVue :open-dialog="addCategoriesDialog" @close-dialog="addCategoriesDialog = false"></DialogAddCategoryVue>

  <v-dialog v-model="saveDialog">
    <v-card>
      <v-card-title>
        <span class="text-h5">Save Diagram</span>
        <br />
        <span v-if="!isNewDiagram" class="text-subtitle-1"
          >{{ diagramStore.diagram.name }} will be overwritten!
          <v-btn id="saveAsNewBtn" @click="isNewDiagram = true">Save as new Diagram</v-btn>
        </span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12" sm="6" md="4">
              <v-text-field v-model="diagramStore.diagram.name" label="Name*" required :disabled="isNewDiagram == false"></v-text-field>
            </v-col>
            <v-col cols="12" sm="6">
              <v-select v-model="diagramStore.diagram.categoryId" :items="categoryNames" item-title="name" item-value="id" label="Kategorie*" required :disabled="isNewDiagram == false"></v-select>
            </v-col>
            <v-col>
              <v-btn cols="12" sm="6" md="4" icon="mdi-folder-plus" :disabled="isNewDiagram == false" @click="addCategoriesDialog = true"></v-btn>
            </v-col>
          </v-row>
        </v-container>
        <small>*indicates required field</small>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="closeSaveDialog"> Close </v-btn>
        <v-btn variant="text" @click="saveDiagram"> Save </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, defineEmits, watch, onMounted } from "vue";
import Category from "../model/diagram/Category";
import diagramService from "../services/diagram.service";
import { useDiagramStore } from "../stores/diagramStore";
import { useAuthUserStore } from "../stores/authUserStore";
import DialogAddCategoryVue from "./DialogAddCategory.vue";

const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();

const categoryNames = ref<Category[]>([]);
const addCategoriesDialog = ref<boolean>(false);

// define props with ts
const props = defineProps<{
  openDialog: boolean;
  selectedDiagramId: number | null;
}>();

// define emit with ts
const emit = defineEmits<{
  (event: "closeDialog"): void;
}>();

const saveDialog = ref<boolean>(props.openDialog);
const isNewDiagram = ref<boolean>(true);

// watch props for changes
watch(
  () => props.openDialog,
  (value) => {
    saveDialog.value = value;
  }
);

watch(
  () => props.selectedDiagramId,
  (value) => {
    if (value == diagramStore.diagram.id) {
      isNewDiagram.value = false;
    } else {
      isNewDiagram.value = true;
    }
  }
);

onMounted(() => {
  let userId = authUserStore.auth.user?.id;
  if (userId != undefined) {
    updateCategories(userId);
  } else {
    console.log("userId is undefined");
  }
});

const updateCategories = (uid: number) => {
  diagramService
    .getCategories(uid)
    .then((response) => {
      categoryNames.value = response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

const closeSaveDialog = () => {
  saveDialog.value = false;
  emit("closeDialog");
};

const saveDiagram = () => {
  // set user id of diagram owner
  if (authUserStore.auth.user != null) {
    diagramStore.diagram.ownerId = authUserStore.auth.user?.id;
  } else {
    throw new Error("User is not logged in");
  }

  if (isNewDiagram.value) {
    diagramService
      .postDiagram(diagramStore.diagram)
      .then((response) => {
        diagramStore.loadDiagram(response.data);
        closeSaveDialog();
      })
      .catch((error) => {
        console.log(error);
        alert("Diagramm konnte nicht gespeichert werden");
      });
  } else {
    diagramService
      .putDiagram(diagramStore.diagram)
      .then(() => {
        closeSaveDialog();
      })
      .catch(() => {
        alert("Diagramm konnte nicht gespeichert werden");
      });
  }
};
</script>

<style scoped lang="scss"></style>
