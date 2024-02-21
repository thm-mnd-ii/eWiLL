<template>
  <DialogCompatibilityIssueVue ref="dialogCompatibility"></DialogCompatibilityIssueVue>
  <BasicBackground>
    <v-card class="login-container">
      <v-tooltip
        ><p>Zur Anmeldung benötigen Sie ihre persönlichen THM-Zugangsdaten.</p>
        <p>Persönliche THM-Kennung bestehend aus 4 Buchstaben und 2 Zahlen,</p>
        <p>sowie Ihr persönliches Passwort</p>
        <template #activator="{ props }"> <v-icon v-bind="props" class="helpIcon">mdi-help-circle</v-icon></template>
      </v-tooltip>
      <v-card-title class="login-title">
        <span>Login</span>

        <!-- <span> <IconEWiLL /> </span> -->
      </v-card-title>
      <v-card-text class="form-container">
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-text-field v-model="userInput" :rules="userRules" label="THM-Kennung" required></v-text-field>
          <v-text-field v-model="passwordInput" :rules="passwordRules" label="THM-Passwort" type="password" required></v-text-field>
          <v-btn v-if="!loading" :disabled="!valid" @click="localLogin"> Login </v-btn>
          <v-progress-circular v-if="loading" indeterminate></v-progress-circular>
        </v-form>
        <v-alert v-if="errorMessage.length != 0" icon="mdi-alert" :text="errorMessage" type="error" variant="outlined"></v-alert>
      </v-card-text>
    </v-card>
  </BasicBackground>
</template>

<script setup lang="ts">
import BasicBackground from "@/components/BasicBackground.vue";

import { ref, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { useAuthUserStore } from "../stores/authUserStore";
import DialogCompatibilityIssueVue from "@/dialog/DialogCompatibilityIssue.vue";

const router = useRouter();
const authUserStore = useAuthUserStore();

const userInput = ref(null);
const passwordInput = ref(null);
const errorMessage = ref("");
const valid = ref(false);
const loading = ref(false);

let userRules = [(v: string) => !!v || "Benutzername is required"];
let passwordRules = [(v: string) => !!v || "Password is required"];

const supportedBrowser = ref(false);
const supportedOS = ref(false);
const supportedScreenSize = ref(false);
const dialogCompatibility = ref<typeof DialogCompatibilityIssueVue>();
const minWidth = 800;
const minHeight = 400;

onMounted(() => {
  _checkCompability();
  window.addEventListener("resize", _checkCompability);
});

onUnmounted(() => {
  window.removeEventListener("resize", _checkCompability);
});

const _checkCompability = () => {
  // Browser Check
  const browserRegex = /Firefox|Chrome|Chromium|Edg\//;
  supportedBrowser.value = browserRegex.test(navigator.userAgent);

  // OS Check
  const osRegex = /Windows NT|Macintosh|Linux/;
  supportedOS.value = osRegex.test(navigator.userAgent);

  // Screen Size
  if (window.innerWidth < minWidth || window.innerHeight < minHeight) {
    supportedScreenSize.value = false;
  } else {
    supportedScreenSize.value = true;
  }
};

onMounted(() => {
  // log token parameter
  const jsessionid = router.currentRoute.value.query.jsessionid?.toString();
  if (jsessionid) {
    tokenLogin(jsessionid);
  }
});

const tokenLogin = (jsessionid: string) => {
  if (!supportedOS.value || !supportedBrowser.value || !supportedScreenSize.value) {
    dialogCompatibility.value?.openDialog();
  } else if (jsessionid) {
    authUserStore
      .tokenLogin(jsessionid)
      .then(() => {
        router.push("/");
      })
      .catch((error) => {
        loading.value = false;
        console.log(error);
        errorMessage.value = "Token Login fehlgeschlagen";
      });
  }
};

const localLogin = () => {
  if (!supportedOS.value || !supportedBrowser.value || !supportedScreenSize.value) {
    dialogCompatibility.value?.openDialog();
  } else {
    errorMessage.value = "";
    loading.value = true;

    if (valid.value && userInput.value && passwordInput.value) {
      const user: { username: string; password: string } = {
        username: userInput.value,
        password: passwordInput.value,
      };

      authUserStore
        .login(user)
        .then(() => {
          setTimeout(() => {
            router.push("/");
          }, 1000);
        })
        .catch((error) => {
          loading.value = false;
          console.log(error);
          if (error.response && error.response.status === 401) {
            errorMessage.value = "Benutzername oder Passwort ist falsch";
          } else if (error.response && error.response.status === 500) {
            errorMessage.value = "Server Error";
          } else if (error.response && error.response.status === 404) {
            errorMessage.value = "Server nicht erreichbar";
          } else if (error.response && error.response.status === 429) {
            errorMessage.value = "Zu viele Anfragen";
          } else {
            errorMessage.value = "Unbekannter Fehler";
          }
        });
    } else {
      loading.value = false;
      errorMessage.value = "Bitte füllen Sie alle Felder aus";
    }
  }
};
</script>

<style scoped lang="scss">
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

  .helpIcon {
    position: relative;
    left: 45%;
  }
}
</style>
