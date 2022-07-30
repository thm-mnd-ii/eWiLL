<script setup>
import { RouterLink, RouterView } from "vue-router"
import IconEWiLL from "./components/icons/IconEWiLL.vue"

</script>

<template>
  <header>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
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
    </nav>
  </header>

  <main class="contentContainer">
    <RouterView />
  </main>
  

  <footer class="fixed-bottom mt-auto py-3 bg-light">
    <div class="container">
      <span class="text-muted">Footer</span>
    </div>
  </footer>
</template>

<style scoped>
  .contentContainer{
    position: absolute;
    height: 100%;
    width: 100%;
  }

  .eWiLL-Logo{
    margin-left: 10px;
    margin-top: -8px;
  }
</style>
