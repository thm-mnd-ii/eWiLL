package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig
import com.wipdev.eWiLL_backend.services.CourseService
import com.wipdev.eWiLL_backend.services.DiagramConfigService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/diagramConfig")
@Tag(name = "Diagram Config", description = "Diagram Config API")
class DiagramConfigController {

    @Autowired
    lateinit var service: DiagramConfigService

    @PostMapping("/create")
    fun create(@RequestBody diagramConfig: DiagramConfig): DiagramConfig = service.create(diagramConfig)
}