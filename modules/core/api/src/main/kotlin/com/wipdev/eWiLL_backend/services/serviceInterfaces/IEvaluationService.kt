package com.wipdev.eWiLL_backend.services.serviceInterfaces

import com.wipdev.eWiLL_backend.database.tables.course.Submission
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import org.springframework.stereotype.Service

@Service
interface IEvaluationService {

    fun eval(submissionRequestPL: SubmissionRequestPL): Long?

    fun getSubmissionResultBySubmissionId(id :Long?): com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult?
    fun getNewestSubmission(userId: Long, taskId: Long): Submission
}