package com.wipdev.eWiLL_backend.services.serviceInterfaces

import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.services.EvaluationService
import org.springframework.stereotype.Service

@Service
interface IEvaluationService {

    fun submit(submissionRequestPL: SubmissionRequestPL): Long?

    fun getSubmissionResultBySubmissionId(id: Long?): com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult?
    fun getNewestSubmission(userId: Long, taskId: Long): EvaluationService.SubmissionWithDiagram
}