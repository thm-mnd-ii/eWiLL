package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.DiagramConfig
import com.wipdev.eWiLL_backend.repository.DiagramConfigRepository
import com.wipdev.eWiLL_backend.repository.UserRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/diagramConfig")
@Tag(name = "Diagram Config", description = "Diagram Config API")
class DiagramConfigController {

    @Autowired
    lateinit var repository: DiagramConfigRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @PostMapping("/")
    fun create(@RequestBody diagramConfig: DiagramConfig): DiagramConfig {
        diagramConfig.id = null
        diagramConfig.creationDate = java.sql.Timestamp(System.currentTimeMillis())
        diagramConfig.lastModified = java.sql.Timestamp(System.currentTimeMillis())

        return repository.save(diagramConfig)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): DiagramConfig {
        return repository.findById(id).get()
    }

    @GetMapping("/all")
    fun getAll(): List<DiagramConfig> {
        return repository.findAll().toList()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody diagramConfig: DiagramConfig): DiagramConfig {
        diagramConfig.id = id
        diagramConfig.lastModified = java.sql.Timestamp(System.currentTimeMillis())
        diagramConfig.creationDate = repository.findById(id).get().creationDate
        return repository.save(diagramConfig)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        repository.deleteById(id)
    }


}