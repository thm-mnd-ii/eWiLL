<template>
  <div class="text-center">
    <v-menu open-on-hover transition="slide-y-transition">
      <template #activator="{ props }">
        <v-btn color="primary" v-bind="props"> Dropdown </v-btn>
      </template>

      <v-list>
        <v-list-item>
          <v-label @click="profile">Profil</v-label>
        </v-list-item>
        <v-list-item>
          <v-label @click="logout">Logout</v-label>
        </v-list-item>
        <v-list-item>
          <v-switch v-model="darkmodeSwitch" color="black" label="Dark Mode" hide-details @change="darken"> </v-switch>
        </v-list-item>
      </v-list>
    </v-menu>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";

const router = useRouter();
const darkmodeSwitch = ref(false);

const profile = () => {
  router.push({ path: "/profile" });
};

const logout = () => {
  console.log("logout");
};

const darken = () => {
  console.log(darkmodeSwitch.value);
};
</script>

<style scoped></style>
