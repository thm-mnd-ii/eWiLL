package com.wipdev.eWiLL_backend.eval.compile

import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL

class DiagramData {

    var id:Long? = null

    var nodes: List<DiagramNode> = emptyList()
    var connections : List<DiagramConnection> = emptyList()

    var rootNodes : List<DiagramNode> = emptyList()


    constructor(diagram: DiagramPL) {
        this.id = diagram.id
        nodes = diagram.entities!!.map { DiagramNode(it) }
        connections = diagram.connections?.map { e->DiagramConnection(getNodeById(e.startEntity!!)!!,getNodeById(e.endEntity!!)!!,e.style!!) } ?: emptyList()

        rootNodes = nodes.filter { e->e.getPreviousNodes().isEmpty() }
    }



    fun getNodeByName(name: String): DiagramNode? {
        return nodes.firstOrNull { it.entity!!.entityName == name }
    }

    private fun getNodeById(id: Long): DiagramNode? {
        return nodes.firstOrNull { it.entity!!.id == id }
    }

    companion object {
        fun compile(diagram:DiagramPL): DiagramData {
            return DiagramData(diagram)
        }
    }
}