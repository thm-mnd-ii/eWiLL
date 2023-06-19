package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.database.tables.course.Course
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.utils.fbs.FbsClient
import com.wipdev.eWiLL_backend.repository.CourseRepository
import com.wipdev.eWiLL_backend.repository.DiagramRepository
import com.wipdev.eWiLL_backend.services.EvaluationService
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils
import com.wipdev.eWiLL_backend.utils.translate.Language
import com.wipdev.eWiLL_backend.utils.translate.Translator
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

    /*@Test
    fun contextLoads() {
        loadDiagramsToDatabase("C:\\Users\\Jonas\\Desktop\\ewilli\\modules\\core\\api\\src\\test\\resources\\diagrams.txt",1,3)
    }*/

    fun runEvalOnDiagrams(diagramIds:List<Long>,taskId:Long,userId:Long){
        //Run submit on all diagrams
        for(diagramId in diagramIds){
            val submissionRequestPL = SubmissionRequestPL(diagramId,taskId,userId)
            val id = evaluationService.submit(submissionRequestPL)!!
            println("$id")
        }

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
