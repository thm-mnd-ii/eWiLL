package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : JpaRepository<SubmissionResult, Long> {


    @Query("SELECT r FROM SubmissionResult r WHERE r.submissionId = :submissionId")
    fun findBySubmissionId(submissionId: Long): SubmissionResult?
}