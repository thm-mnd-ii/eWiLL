import { createApp } from "vue";

// Components
import App from "./App.vue";

// Composables
import router from "./router";
import store from "./store";

// Plugins
import { registerPlugins } from "./plugins";
import vuetify from "./plugins/vuetify";

const app = createApp(App);
registerPlugins();

app.use(router);
app.use(vuetify);
app.use(store);
app.mount("#app");
