package com.wipdev.eWiLL_backend.eval.compile

import com.wipdev.eWiLL_backend.endpoints.payload.requests.Entity

class DiagramNode {

    var diagramData: DiagramData
    var entity: Entity? = null
    var connections: List<DiagramConnection> = emptyList()

    var otherModelNode: DiagramNode? = null


    constructor(entity: Entity, diagramData: DiagramData) {
        this.entity = entity
        this.diagramData = diagramData
    }

    fun getFromConnections(): List<DiagramConnection> {
        return connections.filter { it.from == this }
    }

    fun getToConnections(): List<DiagramConnection> {
        return connections.filter { it.to == this }
    }

    fun getNextNodes(): List<DiagramNode> {
        return getToConnections().map { it.to }
    }

    fun getPreviousNodes(): List<DiagramNode> {
        return getFromConnections().map { it.from }
    }


    fun getEntityName(): String? {
        return entity!!.entityName
    }


}