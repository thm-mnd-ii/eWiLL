<template>
  <v-footer v-if="$router.currentRoute.value.name != 'ViewLogin'" app absolute elevation="3">
    <v-row justify="center" no-gutters class="mt-2">
      <v-btn v-for="link in links" :key="link.name" :to="link.url" color="gray" variant="text" class="mx-2" rounded="xl">
        {{ link.name }}
      </v-btn>
      <v-col class="text-center mt-4" cols="12"
        ><strong> &copy; {{ new Date().getFullYear() }} Technische Hochschule Mittelhessen</strong>
      </v-col>
    </v-row>
  </v-footer>
</template>

<script setup lang="ts">
const links = [
  { name: 'Impressum', url: '/impressum' },
  { name: 'Datenschutz', url: '/datenschutz' }
]
</script>
