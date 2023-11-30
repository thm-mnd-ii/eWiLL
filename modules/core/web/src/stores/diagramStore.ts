import { defineStore } from "pinia";
import DiagramType from "../enums/DiagramType";
import Config from "../model/diagram/Config";
import Connection from "../model/diagram/Connection";
import Diagram from "../model/diagram/Diagram";
import Entity from "../model/diagram/Entity";

interface State {
  diagram: Diagram;
  // increment this key to reload the diagram component
  key: number;
  history: Diagram[];
  historyIndex: number;
  saved: boolean;
}

const config: Config = {
  id: 0,
  diagramType: DiagramType.SERM,
};

export const useDiagramStore = defineStore("diagram", {
  state: (): State => {
    return {
      diagram: {
        id: 0,
        ownerId: 0,
        name: "",
        config: config,
        entities: [] as Entity[],
        connections: [] as Connection[],
      } as Diagram,
      // increment this key to reload the diagram component
      key: 0,
      history: [],
      historyIndex: 0,
      saved: false,
    };
  },
  getters: {
    getDiagram: (state: State) => state.diagram,
  },
  actions: {
    // reset diagram state
    loadDiagram(diagram: Diagram) {
      Object.assign(this, useDiagramStore);
      this.diagram = diagram;
      // reload diagram component
      this.key++;
      // reset history
      this.history = [];
    },
    getMinWidth() {
      let minWidth = 0;
      if (this.diagram.entities && this.diagram.entities.length != 0) {
        this.diagram.entities.forEach((entity) => {
          minWidth = Math.max(minWidth, entity.left + entity.width);
          return minWidth;
        });
      }
      return minWidth;
    },
    getMinHeight() {
      let minHeight = 0;
      if (this.diagram.entities.length != 0) {
        this.diagram.entities.forEach((entity) => {
          minHeight = Math.max(minHeight, entity.top + entity.width / 2);
          return minHeight;
        });
      }
      return minHeight;
    },
    createNewDiagram() {
      this.diagram = {
        id: 0,
        ownerId: 0,
        name: "",
        config: config,
        entities: [] as Entity[],
        connections: [] as Connection[],
      } as Diagram;
      // reload diagram component
      this.key++;
      // reset history
      this.history = [];
      this.historyIndex = 0;
    },
    saveHistory() {
      // console.log("save history");
      // console.log("last Index", this.historyIndex);
      // console.log("history length", this.history.length);
      // console.log("history", this.history);

      // remove all history after current index
      this.history.splice(this.historyIndex, this.history.length);

      // save copy of current state
      this.history.push(JSON.parse(JSON.stringify(this.diagram)));

      this.historyIndex = this.history.length;

      localStorage.setItem("diagram", JSON.stringify(this.diagram));
      // console.log("current Index", this.historyIndex);
    },
    undo() {
      if (this.historyIndex > 1) {
        // console.log("undo");
        // console.log("last Index", this.historyIndex);
        this.historyIndex--;
        // console.log("current Index", this.historyIndex);

        this.diagram = this.history[this.historyIndex - 1];
        this.key++;
      }
      localStorage.setItem("diagram", JSON.stringify(this.diagram));
    },
    redo() {
      if (this.historyIndex < this.history.length) {
        // console.log("redo");
        // console.log("last Index", this.historyIndex);
        this.historyIndex++;
        // console.log("current Index", this.historyIndex);
        this.diagram = this.history[this.historyIndex - 1];
        this.key++;
      }
      localStorage.setItem("diagram", JSON.stringify(this.diagram));
    },
  },
});
