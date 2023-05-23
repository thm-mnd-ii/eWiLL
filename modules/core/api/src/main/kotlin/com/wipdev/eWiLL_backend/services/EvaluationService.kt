package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.database.tables.course.Submission
import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.eval.DiagramEvalEntry
import com.wipdev.eWiLL_backend.eval.IDiagramEvaluator
import com.wipdev.eWiLL_backend.eval.SERMDiagramEvaluator
import com.wipdev.eWiLL_backend.repository.*
import com.wipdev.eWiLL_backend.services.serviceInterfaces.IEvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


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
                result.addComment("Error occurred during evaluation, see logs for more details")
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
            val ruleset = Ruleset()//TODO Change here when configs are needed
            val solutionDiagrams: List<DiagramPL> =
                listOf(diagramRepository.getReferenceById(submission.diagram!!.toLong())).stream()
                    .map { DiagramService.convert(it, configRepository) }.toList()
            val diagramEvalEntry = DiagramEvalEntry(
                taskRepository.getReferenceById(submission.taskId!!),
                ruleset,
                DiagramService.convert(diagram, configRepository),
                solutionDiagrams
            )
            try {
                val result = evaluator.eval(diagramEvalEntry)
                resultRepository.save(result.getResult())

            } catch (e: Exception) {
                var result = resultRepository.getResultsBySubmissionId(submission.id!!)
                if(result == null){
                    result = SubmissionResult()
                }
                result.addComment("Error occurred during evaluation, see logs for more details")
                e.printStackTrace()
                result.correct = false
                result.score = 0f
                result.submissionId = submission.id
                resultRepository.save(result)
            }
        } catch (e: Exception) {
            var result = resultRepository.getResultsBySubmissionId(submission.id!!)
            if(result == null){
                result = SubmissionResult()
            }
            result.addComment("Error preparing Evaluation, see logs for more details")
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
        resultRepository.getResultsBySubmissionId(id!!)


    fun getSubmissions(userId: Long, taskId: Long): List<SubmissionWithDiagram> {
        return submissionRepository.findAllByUserIdAndTaskId(userId, taskId).stream()
            .map { getAsSubmissionWithDiagram(it) }.toList()
    }

    fun getNewestSubmissionsByTaskId(taskId: Long): List<SubmissionWithDiagram> {
        return submissionRepository.getNewestSubmissionsByTaskId(taskId).stream()
            .map { getAsSubmissionWithDiagram(it) }.toList()
    }

    fun getSubmissionsByTaskId(taskId: Long): List<SubmissionWithDiagram> {
        return submissionRepository.getSubmissionsByTaskId(taskId).stream().map { getAsSubmissionWithDiagram(it) }
            .toList()
    }

    private fun getAsSubmissionWithDiagram(submission: Submission): SubmissionWithDiagram {
        val submissionWithDiagram = SubmissionWithDiagram()
        submissionWithDiagram.id = submission.id
        submissionWithDiagram.userId = submission.userId
        submissionWithDiagram.taskId = submission.taskId
        submissionWithDiagram.date = submission.date
        submissionWithDiagram.diagram = submission.getDiagram()
        submissionWithDiagram.attempt = submission.attempt
        return submissionWithDiagram
    }


    class SubmissionWithDiagram {
        var id: Long? = null

        var userId: Long? = null

        var taskId: Long? = null

        var date: String? = null

        var diagram: Diagram? = null

        var attempt: Int? = null


    }

}


