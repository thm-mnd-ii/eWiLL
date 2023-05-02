package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.Submission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SubmissionRepository :JpaRepository<Submission, Long>{
    @Query("SELECT s.id FROM Submission s WHERE s.userId = :userId AND s.taskId = :taskId")
    fun findAllByUserIdAndTaskId(userId: Long, taskId: Long): List<Long>

}
