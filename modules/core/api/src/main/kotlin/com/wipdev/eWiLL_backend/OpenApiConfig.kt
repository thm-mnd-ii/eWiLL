package com.wipdev.eWiLL_backend

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("eWiLL_backend API")
                    .version("0.9")
                    .description("API for managing eWiLL_backend functionality.")
                    .license(
                        io.swagger.v3.oas.models.info.License()
                            .name("Apache 2.0")
                            .url("http://www.apache.org/licenses/LICENSE-2.0.html")
                    )
                    .termsOfService("http://swagger.io/terms/")
                    .contact(
                        io.swagger.v3.oas.models.info.Contact()
                            .name("Jonas Reitz")
                            .email("Jonas.reitz@mni.thm.de")
                            .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                    )
            )
    }
}
