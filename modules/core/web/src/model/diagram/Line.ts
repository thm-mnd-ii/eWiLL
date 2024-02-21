import type CardinalityType from "@/enums/CardinalityType";

export default interface Line {
  id?: number;
  x1?: number;
  y1?: number;
  x2?: number;
  y2?: number;
  style?: CardinalityType;
}
