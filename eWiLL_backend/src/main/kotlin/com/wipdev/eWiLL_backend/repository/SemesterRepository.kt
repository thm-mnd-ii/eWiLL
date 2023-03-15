package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.Semester
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SemesterRepository : JpaRepository<Semester, Long> {
    fun findByName(name: String): Semester?
}