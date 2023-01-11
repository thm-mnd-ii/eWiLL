package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig
import org.springframework.data.jpa.repository.JpaRepository

interface DiagramConfigRepository: JpaRepository<DiagramConfig, Long> {



}
