<template>
  <div>RouterView</div>
  {{ store.state.auth }}
  <button @click="login()">LogIn</button>
  <button @click="logout()">LogOut</button>
</template>

<script setup>
import { useStore } from "vuex";
import { useRouter } from "vue-router";
const router = useRouter();

const store = useStore();

const login = () => {
  const user = { username: "user", password: "user" };

  store.dispatch("auth/login", user).then(
    () => {
      // router.push("/modelling");
    },
    (error) => {
      this.loading = false;
      this.message = (error.response && error.response.data && error.response.data.message) || error.message || error.toString();
    }
  );
};

const logout = () => {
  store.dispatch("auth/logout").then(
    () => {
      router.push("/");
    },
    (error) => {
      this.loading = false;
      this.message = (error.response && error.response.data && error.response.data.message) || error.message || error.toString();
    }
  );
};
</script>
