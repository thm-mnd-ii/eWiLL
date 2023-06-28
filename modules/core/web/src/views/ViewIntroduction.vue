<template>
  <v-parallax class="background">
    <v-card class="card">
      <v-card-title class="title">
        <span>Herzlich willkommen...</span>

        <!-- <span> <IconEWiLL /> </span> -->
      </v-card-title>
      <v-card-text class="text"> ...auf unserer neuen Modellierungsplattform eWiLL. In der aktuellen Version steht Euch die Modellierung zur Verfügung, viele weitere Features folgen in der Zukunft. Eure erstellten Diagramme könnt ihr abspeichern und in verschiedenen Kategorien einsortieren, die Ihr ebenfalls selber anlegen könnt. </v-card-text>
      <v-card-actions>
        <v-spacer>
          <v-btn variant="flat" color="primary-dark" to="/modeling">Hier könnt Ihr modellieren</v-btn>
        </v-spacer>
      </v-card-actions>
    </v-card>
  </v-parallax>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.background {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;

  // gradient from primary dark to primary light
  background: rgb(var(--v-theme-primary-dark));
  background: linear-gradient(150deg, rgb(var(--v-theme-primary-dark)) 25%, rgb(var(--v-theme-primary-light)) 120%);
}

.card {
  width: 700px;
  margin: auto;
  padding: 20px;
  text-align: center;
}

.title {
  font-size: xx-large;
  display: grid;
  grid-gap: 10px;
  grid-template-columns: repeat(1, 1fr);
  align-items: center;
  justify-items: stretch;
  margin-bottom: 15px;
}

.text {
  font-size: large;
  display: grid;
  grid-gap: 20px;
  grid-template-columns: repeat(1, 1fr);
  align-items: center;
  justify-items: stretch;
}
</style>
