<template>
  <v-parallax class="background" src="https://images.unsplash.com/photo-1617957718614-8c23f060c2d0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1332&q=80">
    <v-card class="card">
      <v-card-title class="title">
        <span>404</span>

        <!-- <span> <IconEWiLL /> </span> -->
      </v-card-title>
      <v-card-text class="text"> UH OH! Verirrt? </v-card-text>
      <v-card-actions>
        <v-spacer>
          <v-btn class="btn" to="/">Hier geht es zurück nach Hause</v-btn>
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
}

.card {
  width: 700px;
  margin: auto;
  padding: 20px;
  text-align: center;
}

.title {
  font-size: 4em;
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

.btn {
  background-color: #ff8f45;
}
</style>
