import { createApp } from "vue";
import { createPinia } from 'pinia'
import axios from "axios";

// Components
import App from "./App.vue";

// Composables
import router from "./router";

// Plugins
import { registerPlugins } from "./plugins";
import vuetify from "./plugins/vuetify";

axios.defaults.baseURL = "http://localhost:8080";
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
axios.defaults.headers.common['Access-Control-Allow-Methods'] = 'GET, POST, PATCH, PUT, DELETE, OPTIONS';
axios.defaults.headers.common['Access-Control-Allow-Headers'] = '*';


const pinia = createPinia()
const app = createApp(App);
registerPlugins();

app.use(router);
app.use(vuetify);
app.use(pinia)
app.mount("#app");


