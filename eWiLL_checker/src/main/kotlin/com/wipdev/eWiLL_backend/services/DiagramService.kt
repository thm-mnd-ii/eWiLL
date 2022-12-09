package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.DataclassEntityConverter
import com.wipdev.eWiLL_backend.endpoints.payload.requests.Diagram
import com.wipdev.eWiLL_backend.repository.DiagramRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DiagramService : IDiagramService {

    @Autowired
    private lateinit var diagramRepository: DiagramRepository

    override fun getAll(): List<Diagram> {
        //LoggerFactory.getLogger(ModelService::class.java)
        return diagramRepository.findAll().map { DataclassEntityConverter.convert(it) }
    }

    override fun getById(id: Long): Diagram {
        return DataclassEntityConverter.convert(diagramRepository.findById(id).get())
    }

    override fun create(diagram: Diagram): Diagram {
        return DataclassEntityConverter.convert(diagramRepository.save(DataclassEntityConverter.convert(diagram)))
    }

    override fun update(id: Long, diagram: Diagram): Diagram {
        return DataclassEntityConverter.convert(diagramRepository.save(DataclassEntityConverter.convert(diagram)))
    }

    override fun delete(id: Long): Diagram {
        val diagram = diagramRepository.findById(id).get()
        diagramRepository.delete(diagram)
        return DataclassEntityConverter.convert(diagram)
    }
}