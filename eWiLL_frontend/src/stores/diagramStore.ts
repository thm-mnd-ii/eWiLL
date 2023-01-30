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
}

const config: Config = {
  id: 0,
  diagramType: DiagramType.SERM,
};

// const entityList: Entity[] = [
//   {
//     id: 1,
//     type: EntityType.ENTITY,
//     entityName: "Kunde",
//     top: 124,
//     left: 81,
//     width: 100,
//     attributes: [
//       { name: "KNr", type: AttributeType.PrimaryKey },
//       { name: "Adresse", type: AttributeType.Attribute },
//       { name: "Name", type: AttributeType.Attribute },
//       { name: "Vorname", type: AttributeType.Attribute },
//     ],
//   },
//   {
//     id: 2,
//     type: EntityType.ENTITYRELATIONSHIP,
//     entityName: "Rechnung",
//     top: 122,
//     left: 302,
//     width: 100,
//     attributes: [
//       { name: "RNr", type: AttributeType.PrimaryKey },
//       { name: "KNr", type: AttributeType.ForeignKey },
//       { name: "Datum", type: AttributeType.Attribute },
//     ],
//   },
//   {
//     id: 3,
//     type: EntityType.ENTITY,
//     entityName: "Artikel",
//     top: 207,
//     left: 81,
//     width: 100,
//     attributes: [],
//   },
//   {
//     id: 4,
//     type: EntityType.RELATIONSHIP,
//     entityName: "Rechnungs\npositionen",
//     top: 118,
//     left: 486,
//     width: 100,
//     attributes: [],
//   },
// ];

// const ankerPoints: Connection[] = [
//   {
//     startEntity: 1,
//     startEntityPosition: ConnectorPosition.Right,
//     endEntity: 2,
//     endEntityPosition: ConnectorPosition.Left,
//     style: CardinalityType.ONE_TO_MANY,
//   },
//   {
//     startEntity: 3,
//     startEntityPosition: ConnectorPosition.Right,
//     endEntity: 2,
//     endEntityPosition: ConnectorPosition.Left,
//     style: CardinalityType.ZERO_TO_MANY,
//   },
//   {
//     startEntity: 2,
//     startEntityPosition: ConnectorPosition.Right,
//     endEntity: 4,
//     endEntityPosition: ConnectorPosition.Left,
//     style: CardinalityType.ZERO_TO_ONE,
//   },
// ];

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
    },
  },
});
