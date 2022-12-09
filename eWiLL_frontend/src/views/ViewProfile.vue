<template>
  <pre>{{ store.state.auth }}</pre>
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
import { useStore } from "vuex";
const store = useStore();
const userInformation = ref(store.state.auth);

const status = ref(userInformation.value.status.loggedIn);
const firstName = ref(userInformation.value.user.username);
const email = ref(userInformation.value.user.email);
const role = ref(userInformation.value.user.role);
</script>
