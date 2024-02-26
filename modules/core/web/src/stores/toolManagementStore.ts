import { defineStore } from "pinia";

import type Entity from "@/model/diagram/Entity";
import type Line from "@/model/diagram/Line";
import Mode from "../enums/Mode";
import type Connection from "@/model/diagram/Connection";

import { useDiagramStore } from "./diagramStore";
import ConnectorPosition from "@/enums/ConnectorPosition";
import type Task from "@/model/task/Task";
import type CoursePL from "@/model/course/CoursePL";

interface State {
  showModalAddAttributes: boolean;
  selectedEntity: Entity | null;
  selectedLine: Line | null;
  mode: Mode;
  showIncomingAnkerPoints: boolean;
  newConnection: Connection;
  diagramDiv: HTMLElement | null;
  activeTask: Task | null;
  activeCourse: CoursePL | null;
  highlightedEntityId: number | null;
}

export const useToolManagementStore = defineStore("tool-management", {
  state: (): State => ({
    showModalAddAttributes: false,
    selectedEntity: null,
    selectedLine: null,
    mode: Mode.EDIT,
    showIncomingAnkerPoints: false,
    newConnection: {} as Connection,
    diagramDiv: null,
    activeTask: null,
    activeCourse: null,
    highlightedEntityId: null,
  }),
  getters: {},
  actions: {
    startConnection(id: number, position: ConnectorPosition) {
      this.showIncomingAnkerPoints = true;
      this.newConnection.startEntity = id;
      this.newConnection.startEntityPosition = position;
    },
    endConnection(id: number, position: ConnectorPosition) {
      this.showIncomingAnkerPoints = false;
      this.newConnection.endEntity = id;
      this.newConnection.endEntityPosition = position;

      //initialize style with 0
      this.newConnection.style = 0;

      //add new anker point to diagram
      const diagramStore = useDiagramStore();
      // push as new object to trigger reactivity
      diagramStore.diagram.connections.push({ ...this.newConnection });

      //reset newAnkerPoint
      this.newConnection = {} as Connection;

      //save History
      diagramStore.saveHistory();
    },
    resetConnection() {
      this.showIncomingAnkerPoints = false;
      this.newConnection = {} as Connection;
    },
  },
});
