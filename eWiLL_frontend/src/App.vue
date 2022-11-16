<template>
  <v-app>
    <v-app-bar :elevation="1">
      <v-app-bar-nav-icon @click.stop="showSideBar = !showSideBar" />
      <v-toolbar-title>
        <router-link to="/">
          <IconEWiLL />
        </router-link>
      </v-toolbar-title>
      <v-spacer />
      <div>
        <DropdownUserNav></DropdownUserNav>
      </div>
      <v-btn text color="primary">Log Out</v-btn>
    </v-app-bar>

    <v-navigation-drawer v-model="showSideBar" width="220" expand-on-hover rail>
      <v-list density="compact" active-class="active" nav>
        <v-list-item to="/" active-class="active" prepend-icon="mdi-home-variant" title="Startseite" value="home" />
        <v-list-item to="/modelling" active-class="active" prepend-icon="mdi-pencil-ruler" title="Modellierung" value="modelling" />
        <v-list-item to="/checker" active-class="active" prepend-icon="mdi-code-greater-than" title="Überprüfung" value="checker" />
      </v-list>
    </v-navigation-drawer>

    <v-main class="contentContainer">
      <RouterView />
    </v-main>

    <v-footer app absolute elevation="3">
      <v-row justify="center" no-gutters class="mt-2">
        <v-btn v-for="link in links" :key="link" :to="link.url" color="gray" variant="text" class="mx-2" rounded="xl">
          {{ link.name }}
        </v-btn>
        <v-col class="text-center mt-4" cols="12"
          ><strong> &copy; {{ new Date().getFullYear() }} Technische Hochschule Mittelhessen</strong>
        </v-col>
      </v-row>
    </v-footer>
  </v-app>
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
import IconEWiLL from "./components/icons/IconEWiLL.vue";
import DropdownUserNav from "./components/DropdownUserNav.vue";

import { ref } from "vue";

const showSideBar = ref(true);

const links = [
  { name: "Impressum", url: "/impressum" },
  { name: "Datenschutz", url: "/datenschutz" },
];
</script>

<style scoped lang="scss"></style>
