package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Submission
import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.eval.DiagramEvalEntry
import com.wipdev.eWiLL_backend.eval.IDiagramEvaluator
import com.wipdev.eWiLL_backend.eval.SERMDiagramEvaluator
import com.wipdev.eWiLL_backend.repository.*
import com.wipdev.eWiLL_backend.services.serviceInterfaces.IEvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Service
class EvaluationService: IEvaluationService {

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
    @Autowired
    lateinit var rulesetRepository: RulesetRepository

    /* fun submit(submissionRequestPL: SubmissionRequestPL) : Long?{
        val submission = Submission()
        val diagram = diagramRepository.save(DiagramService.(submissionRequestPL.diagramId))
        submission.diagram = diagram.toString()
        submission.taskId = submissionRequestPL.taskId
        submission.userId = diagram.ownerId
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        submission.date = formattedDateTime
        val submissionSaved = submissionRepository.save(submission)

        return submissionSaved.id
    } */

    override fun eval(submissionRequestPL: SubmissionRequestPL): Long? {
        //Collect Data for evaluation

        val diagram = diagramRepository.getReferenceById(submissionRequestPL.diagramId)



        var submission = Submission()
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        submission.date = formattedDateTime
        submission.diagram = diagram.toString()
        submission.taskId = submissionRequestPL.taskId
        submission.userId = submissionRequestPL.userId

        submission = submissionRepository.save(submission)

        return submission.id

        //Prepare evaluation
        //val diagramEvalEntry = DiagramEvalEntry(task, ruleset, diagram, listOf(solutionDiagram))
        //val evaluator: IDiagramEvaluator =
          //  SERMDiagramEvaluator()//TODO: Change to diffrent Controller when using other models

        //
       // val result = resultRepository.saveEmpty();
       // if (result != null) {
       //     runEvalAsync(evaluator, result.id, diagramEvalEntry)
       //     return result.id
       // }else{
       //     throw Exception("Could not save empty result")
        //}


        //var result = evaluator.eval(diagramEvalEntry)
    }

    override fun getNewestSubmissionIds(userId: Long, taskId: Long): Long {
        return submissionRepository.findFirstByUserIdAndTaskIdOrderByDateDesc(userId, taskId).id!!
    }


    val executor: ExecutorService = Executors.newSingleThreadExecutor()

    fun runEvalAsync(evaluator: IDiagramEvaluator, id: Long?, diagramEvalEntry: DiagramEvalEntry) {
        executor.execute {
            runEval(evaluator, id, diagramEvalEntry)
        }
    }

    fun runEval(evaluator: IDiagramEvaluator, id: Long?, diagramEvalEntry: DiagramEvalEntry) {
        try{
            var result = evaluator.eval(diagramEvalEntry)
            var r = result.getResult()
            r.id = id
            resultRepository.save(r)
        }catch (e:Exception){
            var result = resultRepository.getReferenceById(id!!)
            result.comment = "Error while evaluating"+e.message
            result.correct = false
            result.score = 0f
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


}