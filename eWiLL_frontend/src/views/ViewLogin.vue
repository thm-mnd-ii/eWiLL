<template>
  <div class="login">
    <h1>Login</h1>
    <IconEWiLL class="eWiLL-Logo" /><br />
    <v-btn @click="casLogin">CAS Login</v-btn>
    <div>
      <v-label>or</v-label>
    </div>
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-text-field v-model="emailInput" :rules="emailRules" label="E-Mail" required></v-text-field>
      <v-text-field v-model="passwordInput" :rules="passwordRules" label="Passwort" required></v-text-field>
      <v-btn @click="localLogin">Lokaler Login</v-btn>
    </v-form>
  </div>
</template>

<script setup>
import IconEWiLL from "../components/icons/IconEWiLL.vue";
import { ref } from "vue";

const emailInput = ref(null);
const passwordInput = ref(null);
const valid = ref(true);

const casLogin = () => {
  console.log("CAS Login");
};

let emailRules = [(v) => !!v || "E-mail is required", (v) => /.+@.+\..+/.test(v) || "E-mail must be valid"];

const localLogin = () => {
  console.log("Local Login");
  console.log(emailInput.value);
  console.log(passwordInput.value);
  if (valid.value) console.log("Login with: " + emailInput.value + " and " + passwordInput.value);
};
</script>

<style scoped>
.login {
  position: relative;
  text-align: center;
  left: 33%;
  top: 10%;
  width: 33%;
  padding-bottom: 50px;
  margin-bottom: 50px;
  background-color: rgb(163, 163, 163);
  border: 2px solid rgb(255, 145, 0);
}
.v-btn {
  margin: 40px;
  background-color: rgb(255, 198, 124);
  padding: 5px;
}
input {
  margin: 1%;
  background-color: rgb(255, 255, 255);
}
.eWiLL-Logo {
  margin-top: 3%;
}
.v-label {
  width: 50px;
  height: 40px;
  font-weight: bold;
  background-color: rgb(255, 145, 0);
  font-size: large;
  margin-bottom: 30px;
  border-radius: 50%;
  justify-content: center;
  align-items: center;
}
.v-text-field {
  width: 50%;
  margin-left: 25%;
  margin-right: 25%;
}
</style>
