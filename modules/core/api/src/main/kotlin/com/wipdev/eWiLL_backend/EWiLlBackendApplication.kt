package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.utils.translate.Translator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@SpringBootApplication(scanBasePackages = ["com.wipdev.eWiLL_backend"])
class EWiLlBackendApplication

fun main(args: Array<String>) {
    Translator.loadCacheFromFile()
    runApplication<EWiLlBackendApplication>(*args)

}
