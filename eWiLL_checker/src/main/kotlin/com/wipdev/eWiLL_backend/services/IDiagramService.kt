package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.endpoints.payload.requests.Diagram
import org.springframework.stereotype.Service

@Service
interface IDiagramService {
    fun getAll(): List<Diagram>
    fun getById(id: Long): Diagram
    fun create(diagram: Diagram): Diagram
    fun update(id: Long, diagram: Diagram): Diagram
    fun delete(id: Long): Diagram
}