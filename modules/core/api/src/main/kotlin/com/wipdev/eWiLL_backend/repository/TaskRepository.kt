package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findAllByCourseId(courseId: Long): List<Task>
}