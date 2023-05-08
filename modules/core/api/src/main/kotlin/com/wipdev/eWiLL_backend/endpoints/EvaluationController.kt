package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.services.EvaluationService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/evaluation/v2")
@RestController
@Tag(name = "Submission Controller", description = "Checker API")
class EvaluationController {

    @Autowired
    lateinit var service: EvaluationService



    @PostMapping("/submit")
    @ResponseBody

    fun submit(@RequestBody submissionRequestPL: SubmissionRequestPL): Long? =
        service.eval(submissionRequestPL)

    @GetMapping("/result/{id}")
    @ResponseBody
    fun getResult(@PathVariable id: Long): com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult? =
        service.getEvalResult(id)


    @GetMapping("/submissionIds/{userId}/{taskId}")
    @ResponseBody
    fun getSubmissionIds(@PathVariable userId: Long, @PathVariable taskId: Long): List<Long> =
        service.getSubmissionIds(userId, taskId)

}