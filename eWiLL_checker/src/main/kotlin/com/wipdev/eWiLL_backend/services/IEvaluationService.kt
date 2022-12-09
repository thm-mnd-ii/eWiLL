package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequest
import org.springframework.stereotype.Service

@Service
interface IEvaluationService {

    fun eval(submissionRequest: SubmissionRequest): String
}