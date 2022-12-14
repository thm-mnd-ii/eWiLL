import { defineStore } from "pinia";
import AttributeType from "../enums/AttributeType";
import CardinalityType from "../enums/CardinalityType";
import ConnectorPosition from "../enums/ConnectorPosition";
import DiagramType from "../enums/DiagramType";
import EntityType from "../enums/EntityType";
import Config from "../model/diagram/Config";
import Connection from "../model/diagram/Connection";
import Diagram from "../model/diagram/Diagram";
import Entity from "../model/diagram/Entity";

interface State {
  diagram: Diagram;
}

const config: Config = {
  id: 0,
  diagramType: DiagramType.SERM,
};

const entityList: Entity[] = [
  {
    id: 1,
    type: EntityType.ENTITY,
    entityName: "Kunde",
    top: 124,
    left: 81,
    width: 100,
    attributes: [
      { name: "KNr", type: AttributeType.PrimaryKey },
      { name: "Adresse", type: AttributeType.Attribute },
      { name: "Name", type: AttributeType.Attribute },
      { name: "Vorname", type: AttributeType.Attribute },
    ],
  },
  {
    id: 2,
    type: EntityType.ENTITYRELATIONSHIP,
    entityName: "Rechnung",
    top: 122,
    left: 302,
    width: 100,
    attributes: [
      { name: "RNr", type: AttributeType.PrimaryKey },
      { name: "KNr", type: AttributeType.ForeignKey },
      { name: "Datum", type: AttributeType.Attribute },
    ],
  },
  {
    id: 3,
    type: EntityType.ENTITY,
    entityName: "Artikel",
    top: 207,
    left: 81,
    width: 100,
    attributes: [],
  },
  {
    id: 4,
    type: EntityType.RELATIONSHIP,
    entityName: "Rechnungs\npositionen",
    top: 118,
    left: 486,
    width: 100,
    attributes: [],
  },
];

const ankerPoints: Connection[] = [
  {
    startEntity: 1,
    startEntityPosition: ConnectorPosition.Right,
    endEntity: 2,
    endEntityPosition: ConnectorPosition.Left,
    style: CardinalityType.ONE_TO_MANY,
  },
  {
    startEntity: 3,
    startEntityPosition: ConnectorPosition.Right,
    endEntity: 2,
    endEntityPosition: ConnectorPosition.Left,
    style: CardinalityType.ZERO_TO_MANY,
  },
  {
    startEntity: 2,
    startEntityPosition: ConnectorPosition.Right,
    endEntity: 4,
    endEntityPosition: ConnectorPosition.Left,
    style: CardinalityType.ZERO_TO_ONE,
  },
];

export const useDiagramStore = defineStore("diagram", {
  state: (): State => {
    return {
      diagram: {
        id: 0,
        ownerId: 1,
        name: "Mein Diagramm",
        config: config,
        entities: entityList,
        connections: ankerPoints,
      } as Diagram,
    };
  },
  getters: {
    getDiagram: (state: State) => state.diagram,
  },
  actions: {},
});
