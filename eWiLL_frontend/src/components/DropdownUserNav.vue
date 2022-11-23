<template>
  <div class="menu">
    <v-menu>
      <template #activator="{ props }">
        <v-btn color="info" icon="mdi-account" v-bind="props" />
      </template>
      <v-list>
        <v-list-item v-for="(option, index) in options" :key="index" :value="index">
          <v-list-item-title @click="option.method"><v-icon :icon="option.icon" /> {{ option.title }} </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";
const router = useRouter();

const options = ref([
  { title: "Profil", icon: "mdi-account", method: "profile" },
  { title: "Logout", icon: "mdi-logout", method: "logout" },
]);

// eslint-disable-next-line no-unused-vars
const profile = () => {
  router.push({ path: "/profile" });
};

// eslint-disable-next-line no-unused-vars
const logout = () => {
  console.log("logout");
};
</script>

<style scoped>
.menu {
  margin-right: 10px;
}
</style>
