package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.course.Submission
import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.eval.ResultLevel
import com.wipdev.eWiLL_backend.eval.rules.SubmissionResultWithResultMessages
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
    fun submit(@RequestBody submissionRequestPL: SubmissionRequestPL): Long?{
        return service.submit(submissionRequestPL)
    }


    @GetMapping("/result/{submissionId}/{level}")
    @ResponseBody
    fun getResultByLevel(@PathVariable submissionId: Long,@PathVariable level: ResultLevel): SubmissionResultWithResultMessages?{

        return service.getSubmissionResultBySubmissionId(submissionId,level)
    }



    @GetMapping("/submissionIds/{userId}/{taskId}")
    @ResponseBody
    fun getSubmission(@PathVariable userId: Long, @PathVariable taskId: Long): List<EvaluationService.SubmissionWithDiagram> =
        service.getSubmissions(userId, taskId)

    @GetMapping("/submission/newest/{userId}/{taskId}")
    @ResponseBody
    fun getNewestSubmission(@PathVariable userId: Long, @PathVariable taskId: Long): EvaluationService.SubmissionWithDiagram =
        service.getNewestSubmission(userId, taskId)

    @GetMapping("/submission/newest/{taskId}")
    @ResponseBody
    fun getNewestSubmissionsByTaskId(@PathVariable taskId: Long): List<EvaluationService.SubmissionWithDiagram> =
        service.getNewestSubmissionsByTaskId(taskId)

    @GetMapping("/submission/{taskId}")
    @ResponseBody
    fun getSubmissionsByTaskId(@PathVariable taskId: Long): List<EvaluationService.SubmissionWithDiagram> =
        service.getSubmissionsByTaskId(taskId)
}