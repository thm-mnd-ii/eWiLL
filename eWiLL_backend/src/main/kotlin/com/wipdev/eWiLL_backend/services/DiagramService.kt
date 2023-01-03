package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.endpoints.payload.requests.Connection
import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.endpoints.payload.requests.Entity
import com.wipdev.eWiLL_backend.repository.DiagramRepository
import io.swagger.v3.core.util.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DiagramService : IDiagramService {

    @Autowired
    private lateinit var diagramRepository: DiagramRepository
    override fun getAll(): List<DiagramPL> {
        return diagramRepository.findAll().map { convert(it)}
    }

    override fun getById(id: Long): DiagramPL {
        return convert(diagramRepository.findById(id).get())
    }

    override fun create(diagramPL: DiagramPL): DiagramPL {
        diagramRepository.save(convert(diagramPL))
        return diagramPL
    }

    override fun update(id: Long, diagramPL: DiagramPL): DiagramPL {
        diagramRepository.save(convert(diagramPL))
        return diagramPL
    }

    override fun delete(id: Long): DiagramPL {
        val diagram = diagramRepository.findById(id).get()
        diagramRepository.delete(diagram)
        return convert(diagram)
    }

    override fun getAllByCategoryId(id: Long): List<DiagramPL> {
        return diagramRepository.findAllByCategoryId(id).map { convert(it) }
    }

    override fun getByUserId(userId: Long): List<DiagramPL> {
        return diagramRepository.findAllByUserId(userId).map { convert(it) }
    }


    fun convert(diagram : Diagram) : DiagramPL{
        return DiagramPL(diagram.id,diagram.ownerId,diagram.name,null,parseEntities(diagram.entities),parseConnections(diagram.connections),diagram.categoryId)
    }

    fun parseConnections(connections : String?) : List<Connection>{
        return Json.mapper().readValue(connections,Array<Connection>::class.java).toList()
    }

    fun parseEntities(entities : String?) : List<Entity>{
        return Json.mapper().readValue(entities,Array<Entity>::class.java).toList()
    }

    fun convert(diagramPL : DiagramPL) : Diagram{
        val diagram = Diagram()
        diagram.id = diagramPL.id
        diagram.name = diagramPL.name
        diagram.ownerId = diagramPL.ownerId
        diagram.entities = Json.mapper().writeValueAsString(diagramPL.entities)
        diagram.connections = Json.mapper().writeValueAsString(diagramPL.connections)
        diagram.categoryId = diagramPL.categoryId
        return diagram
    }

}