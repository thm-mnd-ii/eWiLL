package com.wipdev.eWiLL_backend.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.ResourceUtils
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset


//@ControllerAdvice
//class NotFoundHandler {
//
//    @Value("\${spa.default-file}")
//    var defaultFile: String? = null
//
//    val logger = LoggerFactory.getLogger(javaClass)
//
//    @ExceptionHandler(Exception::class)
//    fun renderDefaultPage(): ResponseEntity<String> {
//        logger.info("Rendering default page")
//        try {
//            val indexFile: File = ResourceUtils.getFile(defaultFile!!)
//            val inputStream: FileInputStream = FileInputStream(indexFile)
//            val body: String = StreamUtils.copyToString(inputStream, Charset.defaultCharset())
//            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(body)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Error occurred while rendering default page")
//        }
//    }
//}
//
//@Configuration
//class MvcConfig : WebMvcConfigurer {
//    @Value("\${spring.web.resources.static-locations}")
//    private val staticLocations: String? = null
//
//    val logger = LoggerFactory.getLogger(javaClass)
//    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
//        logger.info("Adding resource handlers")
//        registry.addResourceHandler("/static/**").addResourceLocations(staticLocations!!)
//    }
//}

//@Configuration
//class WebRouterConfig {
//
//    private val logger = LoggerFactory.getLogger(javaClass)
//
//    @Bean
//    fun vueHistoryModeCatchAllRoute(@Value("classpath:/static/index.html") indexHtml: Resource): RouterFunction<ServerResponse> {
//        val serveIndexHtmlFunction = { request: org.springframework.web.reactive.function.server.ServerRequest ->
//            ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml)
//        }
//        val firstApiSegmentExcludes = "api|actuator|js|img|css|fonts|favicon\\.ico"
//        return router {
//            GET("/{path:^(?!${firstApiSegmentExcludes}).*}", serveIndexHtmlFunction)
//            GET("/{path:^(?!${firstApiSegmentExcludes}).*}/**", serveIndexHtmlFunction)
//        }
//    }
//}
