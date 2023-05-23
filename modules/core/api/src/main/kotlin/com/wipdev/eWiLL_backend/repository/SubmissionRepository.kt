package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.Submission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SubmissionRepository : JpaRepository<Submission, Long> {

    fun findFirstByUserIdAndTaskIdOrderByDateDesc(userId: Long, taskId: Long): Submission?


    @Query("SELECT s FROM Submission s WHERE s.userId = :userId AND s.taskId = :taskId")
    fun findAllByUserIdAndTaskId(userId: Long, taskId: Long): List<Submission>


    @Query("SELECT COALESCE(MAX(s.attempt), 0) FROM Submission s WHERE s.userId = :userId AND s.taskId = :taskId")
    fun getAttempts(userId: Long, taskId: Long): Int


    @Query("SELECT s FROM Submission s WHERE s.taskId = :taskId AND s.date = (SELECT MAX(sub.date) FROM Submission sub WHERE sub.taskId = :taskId AND sub.userId = s.userId) ORDER BY s.date DESC")
    fun getNewestSubmissionsByTaskId(taskId: Long): List<Submission>


    @Query("SELECT s FROM Submission s WHERE s.taskId = :taskId")
    fun getSubmissionsByTaskId(taskId: Long): List<Submission>


}
