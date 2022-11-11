import { createApp } from "vue";

// Components
import App from "./App.vue";

// Composables
import router from "./router";

// Plugins
import { registerPlugins } from "@/plugins";
import vuetify from "./plugins/vuetify";

const app = createApp(App);

registerPlugins(app);

app.use(router);
app.use(vuetify);
app.mount("#app");
