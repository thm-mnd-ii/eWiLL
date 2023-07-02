<template>
  <v-parallax class="background">
    <slot />
  </v-parallax>
</template>

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
</style>
