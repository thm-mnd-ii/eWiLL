require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
  env: {
    node: true
  },
  extends: ['eslint:recommended', 'plugin:vue/vue3-recommended', 'prettier', '@vue/eslint-config-typescript', '@vue/eslint-config-typescript'],
  parserOptions: {
    ecmaVersion: 'latest'
  },
  rules: {
    'vue/require-default-prop': 'off',
    'vue/multi-word-component-names': [
      'error',
      {
        ignores: ['Line', 'Entity']
      }
    ],
    'vue/no-mutating-props': 'warn'
    // override/add rules settings here, such as:
    // 'vue/no-unused-vars': 'error'
  }
}
