package com.wipdev.eWiLL_backend.endpoints.payload.requests

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig

/*
 id: number;
  ownerId: number;
  name: string;
  config: Config;
  entities: Entity[];
  connections: Connection[];
 */
data class DiagramPL(var id: Long?, val ownerId: Long?, val name: String?, val config: DiagramConfig?, val entities: List<Entity>?, val connections: List<Connection>?, val categoryId : Long?)
