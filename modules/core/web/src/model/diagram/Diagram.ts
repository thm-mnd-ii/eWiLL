import Config from "./Config";
import Connection from "./Connection";
import Entity from "./Entity";
import Category from "./Category";

interface Diagram {
  id: number;
  ownerId: number;
  name: string;
  config: Config;
  entities: Entity[];
  connections: Connection[];
  // category: Category;
  categoryId: number;
}

export default Diagram;
