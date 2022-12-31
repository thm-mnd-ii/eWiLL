package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
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
        return convert(diagramRepository.save(convert(diagramPL)))
    }

    override fun update(id: Long, diagramPL: DiagramPL): DiagramPL {
        val diagram = diagramRepository.findById(id).get()
        diagram.name = diagramPL.name
        diagram.ownerId = diagramPL.ownerId
        diagram.entities = diagramPL.entities.toString()
        diagram.connections = diagramPL.connections.toString()
        return convert(diagramRepository.save(diagram))
    }

    override fun delete(id: Long): DiagramPL {
        val diagram = diagramRepository.findById(id).get()
        diagramRepository.delete(diagram)
        return convert(diagram)
    }

    override fun getByCategoryId(id: Long): Any {
        return diagramRepository.findByCategoryId(id)
    }


    fun convert(diagram : Diagram) : DiagramPL{
        return Json.mapper().convertValue(diagram, DiagramPL::class.java)
    }

    fun convert(diagramPL : DiagramPL) : Diagram{
        return Json.mapper().convertValue(diagramPL, Diagram::class.java)
    }

}