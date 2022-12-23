import { defineStore } from "pinia";
import AuthService from "../services/auth.service";
import User from "../model/User";

interface State {
  auth: {
    status: {
      loggedIn: boolean;
    };
    user: User | null;
  };
}

const userStorage = localStorage.getItem("user");
let user: User | null = null;
if (userStorage != null) {
  user = JSON.parse(userStorage);
}

const initialState = user
  ? { status: { loggedIn: true }, user }
  : { status: { loggedIn: false }, user: null };

console.log(initialState);

export const useAuthUserStore = defineStore("auth", {
  state: (): State => ({
    auth: initialState,
  }),
  getters: {
    isLoggedIn: (state: State) => state.auth.status.loggedIn === true,
    user: (state: State) => state.auth.user,
  },
  actions: {
    async login(user: { username: string; password: string }) {
      return AuthService.login(user).then(
        (user) => {
          this.auth.status.loggedIn = true;
          this.auth.user = user;
          return Promise.resolve(user);
        },
        (error) => {
          this.auth.status.loggedIn = false;
          this.auth.user = null;
          return Promise.reject(error);
        }
      );
    },
    logout() {
      AuthService.logout();
      this.auth.status.loggedIn = false;
      this.auth.user = null;
      return Promise.resolve();
    },
  },
});
