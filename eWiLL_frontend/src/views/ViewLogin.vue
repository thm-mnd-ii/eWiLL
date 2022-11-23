<template>
  <v-parallax class="background" src="https://images.unsplash.com/photo-1617957718614-8c23f060c2d0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1332&q=80">
    <v-card class="login-container">
      <v-card-title class="login-title">
        <span>Login</span>
        <!-- <span> <IconEWiLL /> </span> -->
      </v-card-title>
      <v-card-text class="form-container">
        <div>
          <v-btn color="green" @click="casLogin">CAS Login</v-btn>
        </div>
        <div>
          <v-chip variant="elevated" color="info"> or </v-chip>
        </div>
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-text-field v-model="emailInput" :rules="emailRules" label="E-Mail" required></v-text-field>
          <v-text-field v-model="passwordInput" :rules="passwordRules" label="Passwort" required></v-text-field>
          <v-btn :disabled="!valid" @click="localLogin">Lokaler Login</v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </v-parallax>
</template>

<script setup>
// import IconEWiLL from "../components/icons/IconEWiLL.vue";
import { ref } from "vue";

const emailInput = ref(null);
const passwordInput = ref(null);
const valid = ref(true);

const casLogin = () => {
  console.log("CAS Login");
};

let emailRules = [(v) => !!v || "E-mail is required", (v) => /.+@.+\..+/.test(v) || "E-mail must be valid"];
let passwordRules = [(v) => !!v || "Password is required"];

const localLogin = () => {
  console.log("Local Login");
  console.log(emailInput.value);
  console.log(passwordInput.value);
  if (valid.value) console.log("Login with: " + emailInput.value + " and " + passwordInput.value);
};
</script>

<style scoped lang="scss">
.background {
  height: 80vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-container {
  width: 400px;
  margin: auto;
  padding: 20px;
  text-align: center;

  .login-title {
    font-size: xx-large;
    display: grid;
    grid-gap: 10px;
    grid-template-columns: repeat(1, 1fr);
    align-items: center;
    justify-items: stretch;
    margin-bottom: 15px;
  }

  .form-container {
    display: grid;
    grid-gap: 20px;
    grid-template-columns: repeat(1, 1fr);
    align-items: center;
    justify-items: stretch;
  }
}
</style>
