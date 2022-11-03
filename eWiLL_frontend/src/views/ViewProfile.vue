<template>
  <div>
    <h3>Profil</h3>
    <p>Vorname: {{ firstName }}</p>
    <p>Nachname: {{ lastName }}</p>
    <p>Email: {{ email }}</p>
    <p>Rolle: {{ role }}</p>
  </div>
</template>

<script setup>
import { ref } from "vue";
const firstName = ref(null);
const lastName = ref(null);
const email = ref(null);
const role = ref(null);
</script>
