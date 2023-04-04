import EntityTyp from "../../enums/EntityType";
import Attribute from "./Attribute";

interface Entity {
  id: number;
  type: EntityTyp;
  entityName: string;
  top: number;
  left: number;
  width: number;
  attributes: Attribute[];
}

export default Entity;
