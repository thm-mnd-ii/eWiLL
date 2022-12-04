import CardinalityType from "../../enums/CardinalityType";
import ConnectorPosition from "../../enums/connectorPosition";

interface Connection {
  startEntity?: number;
  startEntityPosition?: ConnectorPosition;
  endEntity?: number;
  endEntityPosition?: ConnectorPosition;
  style?: CardinalityType;
}

export default Connection;
