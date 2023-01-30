package com.wipdev.eWiLL_backend

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
        println(translations?.get(0)?.translations?.get(0)?.text)
    }

}
