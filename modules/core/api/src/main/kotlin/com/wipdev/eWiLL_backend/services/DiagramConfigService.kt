package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig
import com.wipdev.eWiLL_backend.repository.DiagramConfigRepository
import com.wipdev.eWiLL_backend.services.serviceInterfaces.IDiagramConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DiagramConfigService : IDiagramConfigService {

    @Autowired
    lateinit var repository: DiagramConfigRepository

    override fun create(diagramConfig: DiagramConfig): DiagramConfig {
        diagramConfig.id = null
        return repository.save(diagramConfig)
    }
}