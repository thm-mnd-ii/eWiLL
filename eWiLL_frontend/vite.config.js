import { fileURLToPath, URL } from "url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
// import inject from '@rollup/plugin-inject';
// import jquery from 'jquery'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // inject({
    //   $: 'jquery',
    // }),
  ],
  configureWebpack: {
    devtool: 'source-map'
  },
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
});
