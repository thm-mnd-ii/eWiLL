<template>
  <div class="background">
    <!-- Obere V-Cards -->
    <v-row>
      <v-card class="card" title="Alle Kurse" subtitle="Auflistung aller verfübaren Kurse" text="Hier kannst du alle verfügbaren Kuse sehen. Trage dich in einen Kurs ein, um an den jeweiligen Aufgaben teilnehmen zu können. Die Teilnahme an Kursen ermöglicht es dir deine erlernten Kompetenzen zu überprüfen. Die Ergebnisse deiner überprüfung werden dir anschließend im Dashboard angezeigt.">
        <v-card-actions class="cardAction">
          <v-btn to="/courses">ZU DEN KURSEN</v-btn>
        </v-card-actions>
      </v-card>

      <v-card class="card" title="Meine Kurse" subtitle="Deine aktuellen Kursteilnahmen">
        <v-card-text class="cardText"> Hier siehst du alle Kurse in denen du dieses Semester eingetragen bist. </v-card-text>
        <v-card-text class="cardText">
          <v-card variant="outlined" class="newscard">
            <v-card-title class="newscard_title">Neuigkeiten:</v-card-title>
            <v-card-text>
              <p v-for="item in test" :key="item">{{ item }}</p>
            </v-card-text>
          </v-card>
        </v-card-text>
        <v-card-actions>
          <v-btn to="/courses">ZU MEINEN KURSEN</v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
    <!-- Untere V-Cards -->
    <v-row>
      <v-card class="card" title="Playground" subtitle="Dein ganz persönlicher Bereich zum Modellieren" text="Der Playground stellt dir verschiedene Modellierungsarten zu Verfügung. Im aktuellen Entwicklungsstand besteht nur die Möglichkeit SERM Modelle zu erstellen. Du kannst eine belibige Anzahl an Modellen Speicher und sie von überall aufrufen und bearbeiten.">
        <v-card-actions class="cardAction">
          <v-btn to="/courses">ZUM PLAYGROUND</v-btn>
        </v-card-actions>
      </v-card>

      <v-card class="card" title="Mein Dashboard" subtitle="Bewertung | Feedback | Lernstand | Aktivität">
        <v-card-text class="cardText"> In deinem persönlich Dashboard behältst du immer die Übersicht. Hier werden dir alle Informationen zu Bewertungen, Feedback, Lernstand oder Aktivität angezeigt. </v-card-text>
        <v-card-text class="cardText">
          <v-card variant="outlined" class="newscard">
            <v-card-title class="newscard_title">Neuigkeiten:</v-card-title>
            <v-card-text>
              <p v-for="item in test" :key="item">{{ item }}</p>
            </v-card-text>
          </v-card>
        </v-card-text>
        <v-card-actions>
          <v-btn to="/courses">ZUM DASHBOARD</v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </div>
</template>

<script setup lang="ts">
const dashboardNewsText = "++ Du hast ein Lernziel erreicht ++";
const test = ["News 1", "News 2"];
</script>

<style scoped lang="scss">
.card {
  position: relative;
  left: 17.4%;
  width: 550px;
  margin: 20px;
  margin-top: 50px;
}

.cardText {
  padding-bottom: 0px;
}

.cardAction {
  position: absolute;
  bottom: 0;
}
.newscard {
  padding-bottom: 0px !important;
}

.newscard_title {
  color: #81ba24;
}

.background {
  position: absolute;
  width: 100%;
  height: 90%;
  background-color: #81ba24;
}
</style>
