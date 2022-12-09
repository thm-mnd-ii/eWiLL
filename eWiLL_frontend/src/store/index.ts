import { createStore } from "vuex";
import User from "../model/User";
import { auth } from "./auth.module";

export interface State {
  auth: {
    status: {
      loggedIn: boolean;
    };
    user: User;
  };
}

const store = createStore({
  modules: {
    auth,
  },
});

export default store;
