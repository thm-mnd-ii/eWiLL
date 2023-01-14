package com.wipdev.eWiLL_backend.eval.compile

import com.wipdev.eWiLL_backend.endpoints.payload.requests.Entity

class DiagramNode {

    var entity : Entity? = null
    var connections : List<DiagramConnection> = emptyList()

    constructor(entity: Entity) {
        this.entity = entity
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
}