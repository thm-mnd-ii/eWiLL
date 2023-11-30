<template>
  <DialogAddCategoryVue ref="dialogCategory"></DialogAddCategoryVue>

  <v-dialog v-model="saveDialog" width="75%">
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
        <v-form ref="form" v-model="valid" class="save-diagram-form">
          <v-text-field v-model="diagramName" :rules="[(v: any) => !!v || 'Item is required']" label="Name*" required></v-text-field>
          <v-select v-model="diagramCategory" :rules="[(v: any) => !!v || 'Item is required']" :items="categoryNames" item-title="name" item-value="id" label="Ordner*" required></v-select>
          <v-btn cols="12" sm="6" md="4" icon="mdi-folder-plus" @click="openCategoryDialog"></v-btn>
        </v-form>
        <small>*indicates required field</small>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="_close"> Close </v-btn>
        <v-progress-circular v-if="loading" color="primary" indeterminate size="40"></v-progress-circular>
        <v-btn v-if="!loading" variant="text" @click="saveDiagram"> Save </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import Category from "../model/diagram/Category";
import diagramService from "../services/diagram.service";
import { useDiagramStore } from "../stores/diagramStore";
import { useAuthUserStore } from "../stores/authUserStore";
import DialogAddCategoryVue from "./DialogAddCategory.vue";

const diagramStore = useDiagramStore();
const authUserStore = useAuthUserStore();

const dialogCategory = ref<typeof DialogAddCategoryVue>();

const valid = ref<boolean>(false);
const form = ref();
const diagramName = ref<string>("");
const diagramCategory = ref<number>();
const loading = ref<boolean>(false);
const categoryNames = ref<Category[]>([]);

const saveDialog = ref<boolean>(false);
const isNewDiagram = ref<boolean>(true);

onMounted(() => {
  updateCategories();
});

const updateCategories = () => {
  diagramService
    .getCategories(authUserStore.auth.user?.id as number)
    .then((response) => {
      categoryNames.value = response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

const saveDiagram = () => {
  loading.value = true;
  // set user id of diagram owner
  if (authUserStore.auth.user != null) {
    diagramStore.diagram.ownerId = authUserStore.auth.user?.id;
  } else {
    throw new Error("User is not logged in");
  }

  if (isNewDiagram.value) {
    //validate form
    form.value.validate().then(() => {
      if (valid.value) {
        // set diagram name & category
        diagramStore.diagram.name = diagramName.value;
        diagramStore.diagram.categoryId = diagramCategory.value as number;

        // save diagram
        diagramService
          .postDiagram(diagramStore.diagram)
          .then((result) => {
            // result.data == diagram.id
            diagramStore.diagram.id = result.data;
            localStorage.removeItem("diagram");
            _promiseNewDiagram();
          })
          .catch((error) => {
            console.log(error);
            alert("Diagramm konnte nicht gespeichert werden");
            loading.value = false;
          })
          .finally(() => {
            setTimeout(() => {
              loading.value = false;
            }, 1000);
          });
      } else {
        loading.value = false;
        alert("Form is not valid");
      }
    });
  } else {
    form.value.validate().then(() => {
      if (valid.value) {
        // set diagram name & category
        diagramStore.diagram.name = diagramName.value;
        diagramStore.diagram.categoryId = diagramCategory.value as number;

        diagramService
          .putDiagram(diagramStore.diagram)
          .then(() => {
            localStorage.removeItem("diagram");
            _promiseNewDiagram();
          })
          .catch(() => {
            alert("Diagramm konnte nicht gespeichert werden");
            loading.value = false;
          });
      }
    });
  }
};

const openCategoryDialog = () => {
  dialogCategory.value?.openDialog().then((result: boolean) => {
    if (result) {
      updateCategories();
    }
  });
};

// Promis
const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = (selectedDiagramId: number) => {
  loading.value = false;
  updateCategories();
  if (selectedDiagramId != 0 && selectedDiagramId == diagramStore.diagram.id) {
    isNewDiagram.value = false;
    diagramName.value = diagramStore.diagram.name;
    diagramCategory.value = diagramStore.diagram.categoryId;
  } else {
    isNewDiagram.value = true;
    diagramName.value = "";
    diagramCategory.value = undefined;
  }
  saveDialog.value = true;

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _promiseNewDiagram = () => {
  saveDialog.value = false;
  resolvePromise.value(true);
};

const _close = () => {
  saveDialog.value = false;
  resolvePromise.value(false);
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped lang="scss">
.save-diagram-form {
  // grid in one row
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr;
  grid-template-areas: "name category category";
  grid-column-gap: 10px;
  grid-row-gap: 10px;
  // align items
  align-items: center;
  // set width
  width: 100%;
}
</style>
