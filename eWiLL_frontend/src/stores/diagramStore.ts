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
      if (this.diagram.entities.length != 0) {
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
    },
    // saveHistory() {
    //   // if state doesnt change, dont save history

    //   const currentState = JSON.stringify(this.diagram);
    //   const previousState = JSON.stringify(this.history[this.historyIndex - 1]);

    //   // if initial state, save history
    //   if (previousState == undefined) {
    //     this.history.push(JSON.parse(JSON.stringify(this.diagram)));
    //     this.historyIndex = this.history.length - 1;
    //     return;
    //   }

    //   // if state doesnt change, dont save history
    //   if (currentState == previousState) {
    //     console.log("dont save history");
    //     return;
    //   }

    //   console.log("save history");

    //   console.log(this.historyIndex);
    //   console.log(this.history.length);

    //   console.log(previousState);
    //   console.log(currentState);
    //   console.log(currentState == previousState);

    //   // remove all history after current index
    //   // this.history = this.history.slice(this.historyIndex, this.history.length - 1);

    //   // save copy of current state
    //   this.history.push(JSON.parse(JSON.stringify(this.diagram)));

    //   this.historyIndex = this.history.length;
    // },
    // undo() {
    //   if (this.historyIndex > 0) {
    //     console.log("undo");
    //     this.historyIndex--;
    //     this.diagram = this.history[this.historyIndex];
    //     this.key++;
    //   }
    // },
    // redo() {
    //   if (this.historyIndex < this.history.length - 1) {
    //     console.log("redo");
    //     this.historyIndex++;
    //     this.diagram = this.history[this.historyIndex];
    //     this.key++;
    //   }
    // },
  },
});
