package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.DataclassEntityConverter
import com.wipdev.eWiLL_backend.database.tables.course.Submission
import com.wipdev.eWiLL_backend.endpoints.dataclasses.SubmissionRequest
import com.wipdev.eWiLL_backend.services.EvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/evaluation/v2")
@Controller
class EvaluationController {

    @Autowired
    lateinit var service: EvaluationService

    @PostMapping("/submit")
    //TODO change to not submit the whole submission but only the diagram and taskId+userId
    fun submit(submissionRequest: SubmissionRequest): String {
        return service.eval(submissionRequest)
    }




}