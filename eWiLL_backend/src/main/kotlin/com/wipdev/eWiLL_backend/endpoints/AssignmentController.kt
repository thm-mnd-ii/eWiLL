package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.endpoints.dataclasses.Assignment
import com.wipdev.eWiLL_backend.services.AssignmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/assignments")
@RestController
class AssignmentController() {

    @Autowired
    lateinit var service: AssignmentService

    @GetMapping
    fun getAll() = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @PostMapping
    fun create(assignment: Assignment) = service.create(assignment)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, assignment: Assignment) = service.update(id, assignment)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)


}