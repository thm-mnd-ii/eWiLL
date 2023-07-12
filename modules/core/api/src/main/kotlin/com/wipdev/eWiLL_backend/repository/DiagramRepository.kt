package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.Diagram
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiagramRepository : JpaRepository<Diagram, Long> {
    fun findAllByCategoryId(id: Long): List<Diagram>

    fun findAllByOwnerId(id: Long): List<Diagram>
}