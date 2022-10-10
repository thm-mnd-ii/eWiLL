package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.Assignment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository : JpaRepository<Assignment,Long>