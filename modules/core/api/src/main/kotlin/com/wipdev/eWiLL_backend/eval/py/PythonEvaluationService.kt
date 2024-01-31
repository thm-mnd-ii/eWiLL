package com.wipdev.eWiLL_backend.eval.py

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.database.tables.Task
import com.wipdev.eWiLL_backend.database.tables.course.Submission
import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import com.wipdev.eWiLL_backend.endpoints.payload.requests.Attribute
import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.endpoints.payload.requests.Entity
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.eval.FeedbackLevel
import com.wipdev.eWiLL_backend.eval.StatusLevel
import com.wipdev.eWiLL_backend.eval.rules.ResultMessage
import com.wipdev.eWiLL_backend.eval.rules.ResultMessageType
import com.wipdev.eWiLL_backend.repository.*
import com.wipdev.eWiLL_backend.services.DiagramService
import com.wipdev.eWiLL_backend.utils.TimeUtils
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils
import com.wipdev.eWiLL_backend.utils.translate.Dictionary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.stream.Collectors

@Service
class PythonEvaluationService {

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

    //Executor for async evaluation
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    private val matchingValue = 0.8
    private val ignoreCase = true

    private fun runPythonEval(diagramPayload: DiagramPL, task: Task, solutionDiagrams: List<DiagramPL>): SubmissionResult {
        val result = SubmissionResult() // Creation of the "Result" which contains information about the evaluation result like score and mistakes
        //We can change the datatype of the result if needed

        //TODO:
        //Request evaluation from python server here and fill the result object with the results
        //For example:
        //result = pythonApi.getEvalResult(diagramPayload, task, solutionDiagrams)

        return result
    }



    //Submission Entry Point
    fun submit(submissionRequestPL: SubmissionRequestPL): Long? {
        val diagram = diagramRepository.getReferenceById(submissionRequestPL.diagramId)
        val submission = Submission()

        submission.date = TimeUtils.getDateTimeString()
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

    //Asynchronous evaluation
    fun runEvalAsync(submission: Submission, diagram: Diagram) {
        executor.execute{
            try{
                runEval(submission, diagram)
            }catch (e: Exception){
                val result = SubmissionResult()
                result.addResultMessage(
                    ResultMessage(
                        FeedbackLevel.ERROR,
                        "Error occurred during executing evaluation, see logs for more details",
                        -1,
                        "",
                        "",
                        StatusLevel.NOTHING,
                        ResultMessageType.Other
                    )
                )
                e.printStackTrace()
                result.correct = false
                result.score = 0f
                result.submissionId = submission.id
                resultRepository.save(result)
            } finally {
                println("Evaluation finished")
            }

        }
    }


    private fun runEval(submission: Submission, diagram: Diagram) {
        //A Copy of the diagram is created for evaluation (Does not update(need to change id) if diagram is changed)
        val diagramPayload = DiagramService.convert(diagram,configRepository)
        //Get task from submission
        val task = taskRepository.getReferenceById(submission.taskId!!)
        //List of solution diagrams from task
        val solutionDiagrams: List<DiagramPL> =
            listOf(diagramRepository.getReferenceById(task.solutionModelId!!))
                .stream().map { DiagramService.convert(it, configRepository) }.collect(Collectors.toList())

        prepareEntityAndAttributeNames(diagramPayload,solutionDiagrams)
        //Run python evaluation
        val result = runPythonEval(diagramPayload, task, solutionDiagrams)
        result.submissionId = submission.id
        //Save result to database
        resultRepository.save(result)
    }

    private fun prepareEntityAndAttributeNames(diagramPayload: DiagramPL, solutionDiagrams: List<DiagramPL>) {
        for(entity in diagramPayload.entities!!){
            val possibleNames = Dictionary.getPossibleNames(entity.entityName!!)
            for(solutionDiagram in solutionDiagrams){

                val solutionEntity = getEntityByPossibleNames(solutionDiagram,possibleNames,matchingValue,ignoreCase)
                if(solutionEntity != null){
                    solutionEntity.entityName = entity.entityName
                    for(attribute in entity.attributes!!){
                        val possibleAttributeNames = Dictionary.getPossibleNames(attribute.name!!)
                        val solutionAttribute = getAttributeByPossibleNames(solutionEntity.attributes,possibleAttributeNames,matchingValue,ignoreCase)
                        if(solutionAttribute != null){
                            solutionAttribute.name = attribute.name
                        }
                    }
                }
            }
        }

    }

    private fun getAttributeByPossibleNames(attributes: Array<Attribute>?, possibleAttributeNames: Array<String>, matchingValue: Double, ignoreCase: Boolean): Attribute? {
        return attributes!!.firstOrNull {
            StringFinderUtils.isPresent(
                it.name!!,
                possibleAttributeNames,
                matchingValue,
                ignoreCase
            )
        }
    }

    private fun getEntityByPossibleNames(solutionDiagram: DiagramPL, possibleNames: Array<String>, matchingValue: Double, ignoreCase: Boolean): Entity? {
        return solutionDiagram.entities!!.firstOrNull {
            StringFinderUtils.isPresent(
                it.entityName!!,
                possibleNames,
                matchingValue,
                ignoreCase
            )
        }
    }

}