package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.endpoints.dataclasses.Task
import com.wipdev.eWiLL_backend.services.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/tasks")
@RestController
class AssignmentController() {

    @Autowired
    lateinit var service: TaskService

    @GetMapping
    fun getAll() = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @PostMapping
    fun create(task: Task) = service.create(task)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, task: Task) = service.update(id, task)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)


}