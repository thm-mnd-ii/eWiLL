import type EntityTyp from "@/enums/EntityType";
import type Attribute from "./Attribute";

export default interface Entity {
  id: number;
  type: EntityTyp;
  entityName: string;
  top: number;
  left: number;
  width: number;
  attributes: Attribute[];
}
