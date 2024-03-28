package com.wipdev.eWiLL_backend.services.serviceInterfaces

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig

interface IDiagramConfigService {

    fun create( DiagramConfig: DiagramConfig): DiagramConfig
}