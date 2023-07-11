package com.wipdev.eWiLL_backend.eval.compile

import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils

class DiagramData {

    var id:Long? = null

    var nodes: List<DiagramNode> = emptyList()
    var connections : List<DiagramConnection> = emptyList()

    var rootNodes : List<DiagramNode> = emptyList()


    constructor(diagram: DiagramPL) {
        this.id = diagram.id
        nodes = diagram.entities!!.map { DiagramNode(it,this) }
        connections = diagram.connections?.map { e->DiagramConnection(getNodeById(e.startEntity!!)!!,getNodeById(e.endEntity!!)!!,e.style!!) } ?: emptyList()

        rootNodes = nodes.filter { e->e.getPreviousNodes().isEmpty() }
    }



    fun getNodeByName(name: String?): DiagramNode? {
        if(name == null) return null
        return nodes.firstOrNull { it.entity!!.entityName == name }
    }
    fun getNodeByPossibleNames(possibleNames:Array<String>,matchingValue:Double,ignoreCase:Boolean): DiagramNode? {
        return nodes.firstOrNull { StringFinderUtils.isPresent(it.entity!!.entityName!!,possibleNames,matchingValue,ignoreCase) }
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