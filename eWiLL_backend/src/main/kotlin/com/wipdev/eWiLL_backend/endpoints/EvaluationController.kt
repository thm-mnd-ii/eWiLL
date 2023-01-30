package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.repository.DiagramRepository
import com.wipdev.eWiLL_backend.repository.TaskRepository
import com.wipdev.eWiLL_backend.services.EvaluationService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/evaluation/v2")
@RestController
@Tag(name = "Evaluation/Checker", description = "Checker API")
class EvaluationController {

    @Autowired
    lateinit var service: EvaluationService

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var diagramRepository: DiagramRepository

    @CrossOrigin
    @PostMapping("/submit")
    @ResponseBody
    fun submit(@Parameter submissionRequestPL: SubmissionRequestPL): String? {
        return service.eval(submissionRequestPL).toString()
    }




}