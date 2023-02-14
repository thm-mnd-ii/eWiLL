package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils
import com.wipdev.eWiLL_backend.utils.translate.Language
import com.wipdev.eWiLL_backend.utils.translate.Translator
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EWiLlBackendApplicationTests {

    //Test Translator
    @Test
    fun testTranslator() {
        val text = "Customer"
        val translations = Translator.translate(text, Language.German)
        println(translations)
        assert(translations?.data!!.translations!!.translatedText == "Kunde")
    }

    @Test
    fun testStringSimilarity(){
        val text = "Customer"
        val possibles = Array<String>(1){ "customers" }
        val isPresent = StringFinderUtils.isPresent(text,possibles,0.88,true)
        assert(isPresent)
    }

}
