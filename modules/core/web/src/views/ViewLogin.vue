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
          <v-text-field v-model="userInput" :rules="userRules" label="Benutzername" required></v-text-field>
          <v-text-field v-model="passwordInput" :rules="passwordRules" label="Passwort" type="password" required></v-text-field>
          <v-btn v-if="!loading" :disabled="!valid" @click="localLogin"> Lokaler Login </v-btn>
          <v-progress-circular v-if="loading" indeterminate></v-progress-circular>
        </v-form>
        <v-alert v-if="errorMessage.length != 0" icon="mdi-alert" :text="errorMessage" type="error" variant="outlined"></v-alert>
      </v-card-text>
    </v-card>
  </v-parallax>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";

const router = useRouter();
const authUserStore = useAuthUserStore();

const userInput = ref(null);
const passwordInput = ref(null);
const errorMessage = ref("");
const valid = ref(false);
const loading = ref(false);

const casLogin = () => {
  console.log("CAS Login");
};

let userRules = [(v: string) => !!v || "Benutzername is required"];
let passwordRules = [(v: string) => !!v || "Password is required"];

const localLogin = () => {
  errorMessage.value = "";
  loading.value = true;

  if (valid.value && userInput.value && passwordInput.value) {
    const user: { username: string; password: string } = {
      username: userInput.value,
      password: passwordInput.value,
    };

    authUserStore.login(user).then(
      () => {
        setTimeout(() => {
          router.push("/");
        }, 1000);
      },
      (error) => {
        loading.value = false;
        console.log(error);
        if (error.response.status == 401) {
          errorMessage.value = "Benutzername oder Passwort ist falsch";
        } else if (error.response.status == 500) {
          errorMessage.value = "Server Error";
        } else if (error.response.status == 404) {
          errorMessage.value = "Server nicht erreichbar";
        } else {
          errorMessage.value = "Unbekannter Fehler";
        }
      }
    );
  } else {
    loading.value = false;
    errorMessage.value = "Bitte füllen Sie alle Felder aus";
  }
};
</script>

<style scoped lang="scss">
.background {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-container {
  width: 430px;
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
