package com.wipdev.eWiLL_backend.endpoints


import com.wipdev.eWiLL_backend.endpoints.dataclasses.Diagram
import com.wipdev.eWiLL_backend.services.IDiagramService
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
class DiagramController{

    @Autowired
    lateinit var iDiagramService: IDiagramService



    @GetMapping
    fun getAll() = iDiagramService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long) = iDiagramService.getById(id)

    @PostMapping
    fun create(diagram:Diagram) = iDiagramService.create(diagram)

    @PutMapping("/{id}")
    fun update(@PathVariable id:Long, diagram: Diagram) = iDiagramService.update(id,diagram)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id:Long) = iDiagramService.delete(id)


}