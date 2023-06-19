package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.repository.DiagramRepository
import com.wipdev.eWiLL_backend.repository.ResultRepository
import com.wipdev.eWiLL_backend.services.EvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RequestMapping("/test")
@RestController
class TestController {


    @Autowired
    lateinit var diagramRepository: DiagramRepository

    @Autowired
    lateinit var evaluationService: EvaluationService

    @Autowired
    lateinit var resultRepository: ResultRepository

    @GetMapping("/runEvalTests")
    fun runEvalOnTest(){
        val startTime = System.currentTimeMillis()
        val sampleSize = 20;
        val diagramIds = listOf<Long>(264)//getDiagramsWithHighestId(sampleSize)
        val taskId = 268L
        val userId = 2L
        println("Starting Evaluation on $sampleSize diagrams")
        val submissionIds = runEvalOnDiagrams(diagramIds,taskId,userId)
        val endTime = System.currentTimeMillis()
        val time = endTime - startTime
        println("Async Time: $time")
        println("Waiting for results")
    }

    fun getIsADone(submissionId:Long):Boolean{
        println("SUBMISSION ID: $submissionId")
        if(resultRepository.findBySubmissionId(submissionId) == null){
            return false
        }
        return resultRepository.findBySubmissionId(submissionId)!!.getResultMessages().isNotEmpty()

    }
    fun runEvalOnDiagrams(diagramIds: List<Long?>, taskId:Long, userId:Long): MutableList<Long> {
        //Run submit on all diagrams
        val ids = mutableListOf<Long>()
        for(diagramId in diagramIds){
            val submissionRequestPL = SubmissionRequestPL(diagramId!!,taskId,userId)
            val id = evaluationService.submit(submissionRequestPL)!!
            ids.add(id)
        }
        return ids
    }

    fun getDiagramsWithHighestId(amount:Int):List<Long?>{
        val diagrams = diagramRepository.findAll()
        val sorted = diagrams.sortedByDescending { it.id }
        val ids = sorted.map { it.id }
        return ids.subList(0,amount).stream().collect(java.util.stream.Collectors.toList())
    }
    fun loadDiagramsToDatabase(path:String,categoryToLoadTo:Long,ownerOR :Long) = File(path).useLines { lines ->
        lines.forEach {
            var split = it.split("\\t".toRegex())
            split.forEach(::print)
            println()
            var id = split[0]
            var categoryId = categoryToLoadTo
            var configId = split[2]
            var connections = split[3]
            var entities = split[4]
            var name = split[5]
            var ownerId = ownerOR

            var diagram = Diagram()
            diagram.id = id.toLong()
            diagram.categoryId = categoryId
            diagram.configId = configId.toLong()
            diagram.connections = connections
            diagram.entities = entities
            diagram.name = name
            diagram.ownerId = ownerId

            diagramRepository.save(diagram)

        }
    }
    /*@Test
    fun contextLoads() {
        loadDiagramsToDatabase("C:\\Users\\Jonas\\Desktop\\ewilli\\modules\\core\\api\\src\\test\\resources\\diagrams.txt",1,3)
    }*/
}