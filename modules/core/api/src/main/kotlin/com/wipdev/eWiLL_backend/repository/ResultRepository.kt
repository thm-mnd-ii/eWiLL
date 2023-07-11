package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : JpaRepository<SubmissionResult, Long> {

    @Query("INSERT INTO result (correct, score, comment) VALUES (null, null, null)", nativeQuery = true)
    fun saveEmpty(): SubmissionResult? {
        return findLatestSumbissionResult()
    }


    @Query("SELECT r FROM SubmissionResult r ORDER BY r.id DESC")
    fun findLatestSumbissionResult(): SubmissionResult?
}