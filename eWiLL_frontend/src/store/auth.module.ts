import User from "../model/User";
import AuthService from "../services/auth.service";

const userStorage = localStorage.getItem("user");
let user: User | null = null;
if (userStorage != null) {
  user = JSON.parse(userStorage);
}

const initialState = user
  ? { status: { loggedIn: true }, user }
  : { status: { loggedIn: false }, user: null };

console.log(initialState);

export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    login({ commit }: any, user: { username: string; password: string; }) {
      return AuthService.login(user).then(
        (user) => {
          commit("loginSuccess", user);
          return Promise.resolve(user);
        },
        (error) => {
          commit("loginFailure");
          return Promise.reject(error);
        }
      );
    },
    logout({ commit }: any) {
      AuthService.logout();
      commit("logout");
    },
  },
  mutations: {
    loginSuccess(state: { status: { loggedIn: boolean; }; user: any; }, user: any) {
      state.status.loggedIn = true;
      state.user = user;
    },
    loginFailure(state: { status: { loggedIn: boolean; }; user: null; }) {
      state.status.loggedIn = false;
      state.user = null;
    },
    logout(state: { status: { loggedIn: boolean; }; user: null; }) {
      state.status.loggedIn = false;
      state.user = null;
    },
  },
};
