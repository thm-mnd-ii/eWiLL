package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.database.tables.DiagramHistory
import com.wipdev.eWiLL_backend.repository.DiagramHistoryRepository
import com.wipdev.eWiLL_backend.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DiagramHistoryService {

    @Autowired
    lateinit var diagramHistoryRepository: DiagramHistoryRepository


    private fun saveDiagramHistory(diagramHistory: DiagramHistory) {
        diagramHistoryRepository.save(diagramHistory)
    }

    fun saveDiagramAsHistoryEntry(diagram: Diagram, saveType: String){
        val diagramHistory = DiagramHistory(
            id=null,
            diagramId = diagram.id,
            timeStamp = TimeUtils.getDateTimeString(),
            entities = diagram.entities,
            connections = diagram.connections,
            saveType = saveType
        )

        saveDiagramHistory(diagramHistory)
    }
}