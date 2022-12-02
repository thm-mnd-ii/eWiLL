package com.wipdev.eWiLL_backend

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI()
            .info(Info().title("eWiLL_backend API").version("v0"))
    }
}