import type CardinalityType from "@/enums/CardinalityType";
import type ConnectorPosition from "@/enums/ConnectorPosition";

export default interface Connection {
  startEntity?: number;
  startEntityPosition?: ConnectorPosition;
  endEntity?: number;
  endEntityPosition?: ConnectorPosition;
  style?: CardinalityType;
  gradient?: number;
}
