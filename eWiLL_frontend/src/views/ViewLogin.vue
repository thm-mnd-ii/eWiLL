<template>
  <div class="login">
    <h1>Login</h1>
    <IconEWiLL class="eWiLL-Logo" /><br />
    <button @click="casLogin">CAS Login</button>
    <div>
      <Label>or</Label>
    </div>
    <form id="loginForm">
      <input v-model="emailInput" type="email" name="emailInput" placeholder="E-Mail" /> <br />
      <input v-model="passwordInput" type="password" name="passwordInput" placeholder="Passwort" />
    </form>
    <button @click="localLogin">Lokaler Login</button>
  </div>
</template>

<script setup>
import IconEWiLL from "../components/icons/IconEWiLL.vue";
import { ref } from "vue";

const emailInput = ref(null);
const passwordInput = ref(null);

const casLogin = () => {
  console.log("CAS Login");
};

const localLogin = () => {
  console.log("Local Login");
  console.log(emailInput.value);
  console.log(passwordInput.value);
};
</script>

<style scoped>
.login {
  position: absolute;
  text-align: center;
  left: 33%;
  top: 10%;
  width: 33%;
  height: 60%;
  background-color: rgb(163, 163, 163);
  border: 2px solid rgb(255, 145, 0);
}
button {
  margin: 40px;
}
input {
  margin: 1%;
}
.eWiLL-Logo {
  margin-top: 3%;
}
label {
  width: 50px;
  height: 40px;
  font-weight: bold;
  background-color: rgb(255, 145, 0);
  margin-bottom: 30px;
  border-radius: 50%;
  justify-content: center;
  align-items: center;
}
</style>
