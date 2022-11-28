package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.endpoints.dataclasses.SubmissionRequest
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
interface IEvaluationService {

    fun eval(submissionRequest:SubmissionRequest): String
}