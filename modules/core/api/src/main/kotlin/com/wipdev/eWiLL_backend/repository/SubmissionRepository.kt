package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.Submission
import org.springframework.data.jpa.repository.JpaRepository

interface SubmissionRepository : JpaRepository<Submission, Long> {
}
