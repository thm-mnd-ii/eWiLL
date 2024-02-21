package com.wipdev.eWiLL_backend.services.serviceInterfaces

import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.services.DiagramSaveType
import org.springframework.stereotype.Service

@Service
interface IDiagramService {
    fun getAllDiagramPL(): List<DiagramPL>
    fun getById(id: Long): DiagramPL
    fun create(diagramPL: DiagramPL): Long?
    fun update(id: Long, diagramPL: DiagramPL): DiagramPL
    fun delete(id: Long): DiagramPL
    fun getAllByCategoryId(id: Long): List<DiagramPL>
    fun getByUserId(userId: Long): List<DiagramPL>

}