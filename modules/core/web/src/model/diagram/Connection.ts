import CardinalityType from "../../enums/CardinalityType";
import ConnectorPosition from "../../enums/ConnectorPosition";

interface Connection {
  startEntity?: number;
  startEntityPosition?: ConnectorPosition;
  endEntity?: number;
  endEntityPosition?: ConnectorPosition;
  style?: CardinalityType;
  gradient?: number;
}

export default Connection;
