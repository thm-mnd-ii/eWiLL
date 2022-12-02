package com.wipdev.eWiLL_backend.endpoints


import com.wipdev.eWiLL_backend.endpoints.payload.Diagram
import com.wipdev.eWiLL_backend.services.DiagramService
import com.wipdev.eWiLL_backend.services.IDiagramService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/diagram")
@RestController
@Tag(name = "Diagram", description = "Diagram API")
class DiagramController{

    @Autowired
    lateinit var iDiagramService: DiagramService



    @GetMapping
    fun getAll() = iDiagramService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long) = iDiagramService.getById(id)

    @PostMapping
    fun create(diagram:Diagram) = iDiagramService.create(diagram)

    @PutMapping()
    fun update( diagram: Diagram) = diagram.modelId?.let { iDiagramService.update(it,diagram) }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id:Long) = iDiagramService.delete(id)


}