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

import { ref } from "vue";

const showSideBar = ref(true);

const links = [
  { name: "Impressum", url: "/impressum" },
  { name: "Datenschutz", url: "/datenschutz" },
];
</script>

import DropdownUserNav from "./components/DropdownUserNav.vue";
</script>

<template>
  <header>
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container-fluid">
        <router-link to="/" class="navbar-brand">
          <!-- eWiLL -->
          <IconEWiLL class="eWiLL-Logo" />
        </router-link>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div id="navbarNav" class="collapse navbar-collapse">
          <ul class="navbar-nav">
            <li class="nav-item">
              <router-link to="/modelling" class="nav-link active" aria-current="page">Modellierung</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/checker" class="nav-link active" aria-current="page">Überprüfung</router-link>
            </li>
          </ul>
        </div>
      </div>
      <div>
        <DropdownUserNav></DropdownUserNav>
      </div>
    </nav>
  </header>

  <main class="contentContainer">
    <RouterView />
  </main>

  <footer class="fixed-bottom mt-auto py-3">
    <div class="container d-flex flex-row gap-3">
      <router-link to="/impressum" class="nav-link text-muted" aria-current="page">Impressum</router-link>
      <router-link to="/datenschutz" class="nav-link text-muted" aria-current="page">Datenschutzerklärung</router-link>
    </div>
  </footer>
</template>

<style scoped lang="scss"></style>
