import { defineStore } from 'pinia'

interface State {
  showHeader: boolean
}

export const usePageSettingsStore = defineStore('page-settings', {
  state: (): State => ({
    showHeader: true
  }),
  getters: {},
  actions: {}
})
