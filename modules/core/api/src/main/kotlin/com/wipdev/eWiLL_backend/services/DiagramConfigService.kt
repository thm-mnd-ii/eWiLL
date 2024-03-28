package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig
import com.wipdev.eWiLL_backend.repository.DiagramConfigRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DiagramConfigService {

    @Autowired
    lateinit var repository: DiagramConfigRepository

    fun create(diagramConfig: DiagramConfig): DiagramConfig {
        diagramConfig.id = null
        return repository.save(diagramConfig)
    }

    fun update(diagramConfig: DiagramConfig): DiagramConfig {
        return repository.save(diagramConfig)
    }
}