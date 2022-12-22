import { defineStore } from "pinia";
import Entity from "../model/diagram/Entity";

interface State {
  showModalAddAttributes: boolean;
  selectedEntity: Entity | null;
}

export const useToolManagementStore = defineStore("tool-management", {
  state: (): State => ({
    showModalAddAttributes: false,
    selectedEntity: null,
  }),
  getters: {},
  actions: {},
});
