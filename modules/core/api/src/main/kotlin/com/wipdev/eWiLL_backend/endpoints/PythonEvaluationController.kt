package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.eval.py.PythonEvaluationService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/evaluation/vPython")
@RestController
@Tag(name = "Python Submission Controller", description = "Checker API for Python")
class PythonEvaluationController {

    @Autowired
    lateinit var service: PythonEvaluationService

    @PostMapping("/submit")
    @ResponseBody
    fun submit(@RequestBody submissionRequestPL: SubmissionRequestPL): Long? {
        return service.submit(submissionRequestPL)
    }
}