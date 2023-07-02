<template>
  <!-- Responsive content as grid base 2x2 -->
  <div class="container">
    <!-- Obere V-Cards -->
    <v-card class="card">
      <v-card-title class="card-title">
        <span>Alle Kurse</span>
      </v-card-title>
      <v-card-subtitle class="card-subtitle">
        <span>Auflistung aller verfübaren Kurse</span>
      </v-card-subtitle>
      <v-card-text class="card-text">
        <p>Hier siehst du alle Kurse die dir zur Verfügung stehen. Du kannst dich in jeden Kurs einschreiben und die Kursinhalte nutzen.</p>
      </v-card-text>
      <v-card-actions class="card-action">
        <v-btn to="/course">ZU DEN KURSEN</v-btn>
      </v-card-actions>
    </v-card>

    <v-card class="card">
      <v-card-title class="card-title">
        <span>Meine Kurse</span>
      </v-card-title>
      <v-card-subtitle class="card-subtitle">
        <span>Übersicht über deine Kurse</span>
      </v-card-subtitle>
      <v-card-text class="card-text">
        <p>Hier siehst du alle Kurse in denen du dieses Semester eingetragen bist.</p>
        <br />
        <v-card variant="outlined" class="newscard">
          <v-card-title class="newscard_title">Neuigkeiten:</v-card-title>
          <v-card-text>
            <p v-for="item in test" :key="item">{{ item }}</p>
          </v-card-text>
        </v-card>
      </v-card-text>

      <v-card-actions>
        <v-btn to="/course">ZU MEINEN KURSEN</v-btn>
      </v-card-actions>
    </v-card>

    <!-- Untere V-Cards -->
    <v-card class="card">
      <v-card-title class="card-title">Playground</v-card-title>
      <v-card-subtitle class="card-subtitle">Dein ganz persönlicher Bereich zum Modellieren</v-card-subtitle>
      <v-card-text class="card-text">Der Playground stellt dir verschiedene Modellierungsarten zu Verfügung. Im aktuellen Entwicklungsstand besteht nur die Möglichkeit SERM Modelle zu erstellen. Du kannst eine belibige Anzahl an Modellen Speicher und sie von überall aufrufen und bearbeiten.</v-card-text>
      <v-card-actions class="card-action">
        <v-btn to="/modeling">ZUM PLAYGROUND</v-btn>
      </v-card-actions>
    </v-card>

    <v-card class="card">
      <v-card-title class="card-title">Mein Dashboard</v-card-title>
      <v-card-subtitle class="card-subtitle">Bewertung | Feedback | Lernstand | Aktivität</v-card-subtitle>
      <v-card-text class="card-text"> In deinem persönlich Dashboard behältst du immer die Übersicht. Hier werden dir alle Informationen zu Bewertungen, Feedback, Lernstand oder Aktivität angezeigt. </v-card-text>
      <v-card-text class="card-text">
        <v-card variant="outlined" class="newscard">
          <v-card-title class="newscard_title">Neuigkeiten:</v-card-title>
          <v-card-text>
            <p v-for="item in test" :key="item">{{ item }}</p>
          </v-card-text>
        </v-card>
      </v-card-text>
      <v-card-actions>
        <v-btn to="/dashboard">ZUM DASHBOARD</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script setup lang="ts">
const test = ["News 1", "News 2"];
</script>

<style scoped lang="scss">
.container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  row-gap: 2rem;
  column-gap: 1.5rem;
  grid-template-areas:
    "card card"
    "card card";
  padding: 2.5rem 2rem;
  background-color: rgb(var(--v-theme-primary));
}

.card {
  // fit to parent
  width: 100%;
  height: 100%;
  // center content
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card-title {
  padding-bottom: 0;
}

.card-subtitle {
  color: rgb(var(--v-theme-primary-dark));
}

.newscard_title {
  color: rgb(var(--v-theme-primary-dark));
}
</style>
