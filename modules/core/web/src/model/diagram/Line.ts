import CardinalityType from "../../enums/CardinalityType";

interface Line {
  id?: number;
  x1?: number;
  y1?: number;
  x2?: number;
  y2?: number;
  style?: CardinalityType;
}

export default Line;
