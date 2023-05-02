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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    lateinit var rulesetRepository: RulesetRepository

    @Autowired
    lateinit var submissionRepository : SubmissionRepository

    fun submit(submissionRequestPL: SubmissionRequestPL) : Long?{
        val submission = Submission()
        val diagram = diagramRepository.save(DiagramService.convert(submissionRequestPL.diagramPL))
        submission.diagram = diagram.toString()
        submission.taskId = submissionRequestPL.taskId
        submission.userId = diagram.ownerId
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        submission.date = formattedDateTime
        val submissionSaved = submissionRepository.save(submission)

        return submissionSaved.id
    }

    override fun eval(submissionRequestPL: SubmissionRequestPL): Long? {
        //Collect Data for evaluation
        val task = taskRepository.findById(submissionRequestPL.taskId.toLong()).get()
        val ruleset = task.rulesetId?.let { rulesetRepository.findById(it).get() }
        val diagram = submissionRequestPL.diagramPL;
        val solutionDiagram = DiagramService.convert(diagramRepository.findById(task.solutionModelId!!).get(), configRepository)

        //Prepare evaluation
        val diagramEvalEntry = DiagramEvalEntry(task, ruleset, diagram, listOf(solutionDiagram))
        val evaluator: IDiagramEvaluator =
            SERMDiagramEvaluator()//TODO: Change to diffrent Controller when using other models

        //
        val result = resultRepository.saveEmpty();
        if (result != null) {
            runEvalAsync(evaluator, result.id, diagramEvalEntry)
            return result.id
        }else{
            throw Exception("Could not save empty result")
        }


        //var result = evaluator.eval(diagramEvalEntry)
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

    override fun getEvalResult(id: Long?): SubmissionResult? = resultRepository.findById(id!!).get()



}