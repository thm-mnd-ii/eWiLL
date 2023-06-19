package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.repository.DiagramRepository
import com.wipdev.eWiLL_backend.repository.ResultRepository
import com.wipdev.eWiLL_backend.repository.SubmissionRepository
import com.wipdev.eWiLL_backend.services.EvaluationService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class EWiLlBackendApplicationTests {

    @Autowired
    lateinit var diagramRepository: DiagramRepository

    @Autowired
    lateinit var evaluationService: EvaluationService

    @Autowired
    lateinit var resultRepository: ResultRepository

    /*@Test
    fun contextLoads() {
        loadDiagramsToDatabase("C:\\Users\\Jonas\\Desktop\\ewilli\\modules\\core\\api\\src\\test\\resources\\diagrams.txt",1,3)
    }*/

@Test
    fun runEvalOnTest(){
        val startTime = System.currentTimeMillis()
        val sampleSize = 20;
        val diagramIds = getDiagramsWithHighestId(sampleSize)
        val taskId = 268L
        val userId = 2L
        println("Starting Evaluation on $sampleSize diagrams")
        val submissionIds = runEvalOnDiagrams(diagramIds,taskId,userId)
        val endTime = System.currentTimeMillis()
        val time = endTime - startTime
        println("Async Time: $time")

        while(getIsADone(submissionIds[submissionIds.size-1])){
            Thread.sleep(100)
        }
        val endTime2 = System.currentTimeMillis()
        val time2 = endTime2 - startTime
        println("Sync Time: $time2")
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


    /*Test Translator
    @Test
    fun testTranslator() {
        val text = "Customer"
        val translations = Translator.translate(text, Language.German)
        println(translations)
        assert(translations?.data!!.translations!!.translatedText == "Kunde")
    }*/

    /*@Test
    fun testStringSimilarity(){
        val text = "Customer"
        val possibles = Array<String>(1){ "customers" }
        val isPresent = StringFinderUtils.isPresent(text,possibles,0.88,true)
        assert(isPresent)
    }

    @Autowired
    lateinit var courseRepository: CourseRepository
*/


}
