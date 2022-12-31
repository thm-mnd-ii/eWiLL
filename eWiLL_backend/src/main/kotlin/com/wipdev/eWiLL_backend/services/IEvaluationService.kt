package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import org.springframework.stereotype.Service

@Service
interface IEvaluationService {

    fun eval(submissionRequestPL: SubmissionRequestPL): String
}