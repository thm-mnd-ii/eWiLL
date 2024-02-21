package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequestPL
import com.wipdev.eWiLL_backend.repository.DiagramRepository
import com.wipdev.eWiLL_backend.repository.ResultRepository
import com.wipdev.eWiLL_backend.repository.SubmissionRepository
import com.wipdev.eWiLL_backend.security.auth.JwtUtils
import com.wipdev.eWiLL_backend.services.EvaluationService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class EWiLlBackendApplicationTests {




    @Test
    fun tester() {
        JwtUtils.getSecretKey()
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
