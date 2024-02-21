import type DiagramSaveType from "@/enums/DiagramSaveType";
import type Config from "./Config";
import type Connection from "./Connection";
import type Entity from "./Entity";

export default interface Diagram {
  id: number;
  ownerId: number;
  name: string;
  config: Config;
  entities: Entity[];
  connections: Connection[];
  categoryId: number;
  diagramSaveType: DiagramSaveType;
}
