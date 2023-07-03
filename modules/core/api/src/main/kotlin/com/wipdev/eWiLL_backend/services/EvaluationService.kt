package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.database.tables.course.Submission
import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.eval.*
import com.wipdev.eWiLL_backend.eval.rules.ResultMessage
import com.wipdev.eWiLL_backend.eval.rules.ResultMessageType
import com.wipdev.eWiLL_backend.eval.rules.SubmissionResultWithResultMessages
import com.wipdev.eWiLL_backend.repository.*
import com.wipdev.eWiLL_backend.services.serviceInterfaces.IEvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.stream.Collectors.toList


@Service
class EvaluationService : IEvaluationService {

    @Autowired
    lateinit var resultRepository: ResultRepository

    @Autowired
    lateinit var diagramRepository: DiagramRepository

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var configRepository: DiagramConfigRepository

    @Autowired
    lateinit var submissionRepository: SubmissionRepository


    override fun submit(submissionRequestPL: SubmissionRequestPL): Long? {
        val diagram = diagramRepository.getReferenceById(submissionRequestPL.diagramId)
        val submission = Submission()

        submission.date = getDateTimeString()
        submission.setDiagram(diagram)
        submission.taskId = submissionRequestPL.taskId
        submission.userId = submissionRequestPL.userId
        submission.attempt =
            submissionRepository.getAttempts(submissionRequestPL.userId, submissionRequestPL.taskId) + 1
        val submissionId = submissionRepository.save(submission).id

        //start evaluation in new thread
        runEvalAsync(submission, diagram)


        return submissionId
    }

    val executor: ExecutorService = Executors.newSingleThreadExecutor()
    fun runEvalAsync(submission: Submission, diagram: Diagram) {
        executor.execute {
            try {
                runEval(submission, diagram)
            } catch (e: Exception) {
                val result = SubmissionResult()
                result.addResultMessage(ResultMessage(ResultLevel.ERROR, "Error occurred during executing evaluation, see logs for more details", -1, "", HighlightLevel.NOTHING, ResultMessageType.Other))
                e.printStackTrace()
                result.correct = false
                result.score = 0f
                result.submissionId = submission.id
                resultRepository.save(result)
            }finally {
                println("Evaluation finished")
            }
        }
    }

    fun runEval(submission: Submission, diagram: Diagram) {
        try {
            val evaluator: IDiagramEvaluator = SERMDiagramEvaluator()
            val task = taskRepository.getReferenceById(submission.taskId!!)
            val solutionDiagrams: List<DiagramPL> =
                listOf(diagramRepository.getReferenceById(task.solutionModelId!!))
                    .stream().map { DiagramService.convert(it, configRepository) }.collect(toList())
            val diagramEvalEntry = DiagramEvalEntry(
                taskRepository.getReferenceById(submission.taskId!!),
                DiagramService.convert(diagram, configRepository),
                solutionDiagrams
            )
            try {
                val result = evaluator.eval(diagramEvalEntry)
                resultRepository.save(result.getResult(submission.id!!))

            } catch (e: Exception) {
                var result = resultRepository.findBySubmissionId(submission.id!!)
                if(result == null)
                    result = SubmissionResult()
                result.addResultMessage(ResultMessage(ResultLevel.DEBUG,"Error occurred during evaluation, see logs for more details",-1,"",HighlightLevel.NOTHING,ResultMessageType.Other))
                e.printStackTrace()
                result.correct = false
                result.score = 0f
                result.submissionId = submission.id
                resultRepository.save(result)
            }
        } catch (e: Exception) {
            var result = resultRepository.findBySubmissionId(submission.id!!)
            if(result == null)
                result = SubmissionResult()
            result.addResultMessage(ResultMessage(ResultLevel.DEBUG,"Error preparing Evaluation, see logs for more details",-1,"",HighlightLevel.NOTHING,ResultMessageType.Other))
            e.printStackTrace()
            result.correct = false
            result.score = 0f
            result.submissionId = submission.id
            resultRepository.save(result)
        }

    }

    private fun getDateTimeString(): String? {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    override fun getNewestSubmission(userId: Long, taskId: Long): SubmissionWithDiagram {
        return getAsSubmissionWithDiagram(
            submissionRepository.findFirstByUserIdAndTaskIdOrderByDateDesc(userId, taskId)!!
        )
    }


    override fun getSubmissionResultBySubmissionId(id: Long?): SubmissionResult? =
        resultRepository.findBySubmissionId(id!!)


    fun getSubmissions(userId: Long, taskId: Long): List<SubmissionWithDiagram> {
        return submissionRepository.findAllByUserIdAndTaskId(userId, taskId).stream()
            .map { getAsSubmissionWithDiagram(it) }.collect(toList())
    }

    fun getNewestSubmissionsByTaskId(taskId: Long): List<SubmissionWithDiagram> {
        return submissionRepository.getNewestSubmissionsByTaskId(taskId).stream()
            .map { getAsSubmissionWithDiagram(it) }.collect(toList())
    }

    fun getSubmissionsByTaskId(taskId: Long): List<SubmissionWithDiagram> {
        return submissionRepository.getSubmissionsByTaskId(taskId).stream().map { getAsSubmissionWithDiagram(it) }
            .collect(toList())
    }

    private fun getAsSubmissionWithDiagram(submission: Submission): SubmissionWithDiagram {
        val submissionWithDiagram = SubmissionWithDiagram()
        submissionWithDiagram.id = submission.id
        submissionWithDiagram.userId = submission.userId
        submissionWithDiagram.taskId = submission.taskId
        submissionWithDiagram.date = submission.date
        submissionWithDiagram.diagram = submission.getDiagram()?.let { DiagramService.convert(it, configRepository) }
        submissionWithDiagram.attempt = submission.attempt
        return submissionWithDiagram
    }

    fun getSubmissionResultBySubmissionId(id: Long, level: ResultLevel): SubmissionResultWithResultMessages? {
        var result = resultRepository.findBySubmissionId(id)!!;
        var comments = result.getResultMessages().stream().filter { it.resultLevel.ordinal <= level.ordinal }.collect(toList())

        return SubmissionResultWithResultMessages(result.id, result.correct, result.score,comments, result.submissionId)
    }


    class SubmissionWithDiagram {
        var id: Long? = null

        var userId: Long? = null

        var taskId: Long? = null

        var date: String? = null

        var diagram: DiagramPL? = null

        var attempt: Int? = null


    }

}


