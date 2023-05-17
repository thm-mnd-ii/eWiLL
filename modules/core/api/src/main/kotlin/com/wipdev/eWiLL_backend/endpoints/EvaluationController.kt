package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.course.Submission
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.services.EvaluationService
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

    @GetMapping("/result/{submissionId}")
    @ResponseBody
    fun getResult(@PathVariable submissionId: Long): com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult? =
        service.getSubmissionResultBySubmissionId(submissionId)


    @GetMapping("/submissionIds/{userId}/{taskId}")
    @ResponseBody
    fun getSubmission(@PathVariable userId: Long, @PathVariable taskId: Long): List<Submission> =
        service.getSubmissions(userId, taskId)

    @GetMapping("/submission/newest/{userId}/{taskId}")
    @ResponseBody
    fun getNewestSubmission(@PathVariable userId: Long, @PathVariable taskId: Long): Submission =
        service.getNewestSubmission(userId, taskId)

    @GetMapping("/submission/newest/{taskId}")
    @ResponseBody
    fun getNewestSubmissionsByTaskId(@PathVariable taskId: Long): List<Submission> =
        service.getNewestSubmissionsByTaskId(taskId)

    @GetMapping("/submission/{taskId}")
    @ResponseBody
    fun getSubmissionsByTaskId(@PathVariable taskId: Long): List<Submission> =
        service.getSubmissionsByTaskId(taskId)
}