/* eslint-env node */
require("@rushstack/eslint-patch/modern-module-resolution");

module.exports = {
  env: {
    node: true,
  },
  extends: [
    'eslint:recommended',
    "plugin:vue/vue3-recommended",
    "prettier"
  ],
  parser: "vue-eslint-parser",
  parserOptions: {
    sourceType: "module"
  },
  rules: {
    // override/add rules settings here, such as:
    // 'vue/no-unused-vars': 'error'
  }
}
