<template>
  <pre>{{ userInformation }}</pre>
  <div>
    <h3>Profil</h3>
    <p>LogInStatus: {{ status }}</p>
    <p>Vorname: {{ firstName }}</p>
    <p>Email: {{ email }}</p>
    <pre>Rolle: {{ role }}</pre>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useAuthUserStore } from "../stores/authUserStore";

const authUserStore = useAuthUserStore();
const userInformation = authUserStore.user;

const status = ref(authUserStore.isLoggedIn);
const firstName = ref(userInformation?.username);
const email = ref(userInformation?.email);
const role = ref(userInformation?.roles);
</script>
