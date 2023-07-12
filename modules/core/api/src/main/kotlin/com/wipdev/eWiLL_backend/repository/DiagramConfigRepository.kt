package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiagramConfigRepository : JpaRepository<DiagramConfig, Long>
