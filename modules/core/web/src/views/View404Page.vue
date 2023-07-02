<template>
  <BasicBackground>
    <v-card class="card">
      <v-card-title class="title">
        <span>
          <IconEWiLL class="icon" />
        </span>
        <br />
        <span>404</span>
      </v-card-title>
      <v-card-text class="text"> UH OH! Verirrt? </v-card-text>
      <v-card-actions>
        <v-spacer>
          <v-btn color="primary-dark" variant="flat" to="/">Hier geht es zurück nach Hause</v-btn>
        </v-spacer>
      </v-card-actions>
    </v-card>
  </BasicBackground>
</template>

<script setup lang="ts">
import BasicBackground from "@/components/BasicBackground.vue";
import IconEWiLL from "@/components/icons/IconEWiLL.vue";
</script>

<style scoped lang="scss">
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

.icon {
  //svg size
  width: 200px;
  height: auto;
}
</style>
