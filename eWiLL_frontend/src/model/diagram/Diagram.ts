import Config from "./Config";
import Connection from "./Connection";
import Entity from "./Entity";

interface Diagram {
  id: number;
  ownerId: number;
  name: string;
  config: Config;
  entities: Entity[];
  connections: Connection[];
}

export default Diagram;
