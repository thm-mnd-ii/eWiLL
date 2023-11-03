package com.wipdev.eWiLL_backend.endpoints.payload.requests

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig
import com.wipdev.eWiLL_backend.services.DiagramSaveType


data class DiagramPL(
    var id: Long?,
    var ownerId: Long?,
    var name: String?,
    val config: DiagramConfig?,
    val entities: List<Entity>?,
    val connections: List<Connection>?,
    val categoryId: Long?,
    val diagramSaveType: DiagramSaveType? = DiagramSaveType.MANUAL
)
