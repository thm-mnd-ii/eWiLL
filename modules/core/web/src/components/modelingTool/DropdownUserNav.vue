<template>
  <div class="menu">
    <!-- Aufgrund eines bugs in Vuetify 3 kann diese funktion nicht verwendet werden -->
    <!-- <v-menu>
      <template #activator="{ props }">
        <v-btn color="info" icon="mdi-account" v-bind="props" />
      </template>
      <v-list>
        <v-list-item v-for="(option, index) in options" :key="index" :value="index">
          <v-list-item-title @click="option.method"><v-icon :icon="option.icon" /> {{ option.title }} </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu> -->
    <v-btn prepend-icon="mdi-account" @click="profile">profile</v-btn>
    <v-btn prepend-icon="mdi-logout" @click="logout">logout</v-btn>
  </div>
</template>

<script setup lang="ts">
//import { ref } from "vue";
import { useAuthUserStore } from "../../stores/authUserStore";
import { useRouter } from "vue-router";
const router = useRouter();

// const options = ref([
//   { title: "Profil", icon: "mdi-account", method: "profile" },
//   { title: "Logout", icon: "mdi-logout", method: "logout" },
// ]);

const profile = () => {
  router.push({ path: "/profile" });
};

const logout = () => {
  const authUserStore = useAuthUserStore();
  authUserStore.logout().then(
    () => {
      router.push("/login");
    },
    (error) => {
      console.log(error);
    }
  );
};
</script>

<style scoped>
.menu {
  margin-right: 10px;
}
</style>
