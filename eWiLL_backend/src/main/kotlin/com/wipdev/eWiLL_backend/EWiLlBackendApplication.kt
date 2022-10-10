package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.endpoints.AssignmentController
import com.wipdev.eWiLL_backend.endpoints.ModelController
import com.wipdev.eWiLL_backend.repository.AssignmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SpringBootApplication(scanBasePackages = ["com.wipdev.eWiLL_backend"])
class EWiLlBackendApplication{




}

fun main(args: Array<String>) {
	runApplication<EWiLlBackendApplication>(*args)
}
