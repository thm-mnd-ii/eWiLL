<template>
  <div>RouterView</div>
  <button @click="login()">LogIn</button>
</template>

<script setup>
import AuthService from "../services/auth.service";

const login = () => {
  const user = { username: "test", password: "test" };
  console.log("login");
  console.log(user);

  AuthService.login(user);
};
</script>
