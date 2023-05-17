package com.wipdev.eWiLL_backend.repository

import org.springframework.data.jpa.repository.Query
import com.wipdev.eWiLL_backend.database.tables.course.Submission
import org.springframework.data.jpa.repository.JpaRepository

interface SubmissionRepository : JpaRepository<Submission, Long> {

    @Query("SELECT s.id FROM Submission s WHERE s.userId = :userId AND s.taskId = :taskId ORDER BY s.date DESC")
    fun findFirstByUserIdAndTaskIdOrderByDateDesc(userId: Long, taskId: Long): Submission

    @Query("SELECT s FROM Submission s WHERE s.userId = :userId AND s.taskId = :taskId")
    fun findAllByUserIdAndTaskId(userId: Long, taskId: Long): List<Submission>


    @Query("SELECT MAX(s.attempt) FROM Submission s WHERE s.userId = :userId AND s.taskId = :taskId")
    fun getAttempts(userId: Long, taskId: Long): Int

    @Query("SELECT s FROM Submission s WHERE s.taskId = :taskId ORDER BY s.date DESC")
    fun getNewestSubmissionsByTaskId(taskId: Long): List<Submission>

    @Query("SELECT s FROM Submission s WHERE s.taskId = :taskId")
    fun getSubmissionsByTaskId(taskId: Long): List<Submission>


}
