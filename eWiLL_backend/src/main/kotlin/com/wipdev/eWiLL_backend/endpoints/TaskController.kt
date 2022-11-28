package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.endpoints.dataclasses.Task
import com.wipdev.eWiLL_backend.services.TaskService
import org.intellij.lang.annotations.Pattern
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/tasks")
@RestController
class TaskController() {

    @Autowired
    lateinit var service: TaskService

    @GetMapping("/{courseId}/{id}")
    fun getAll(@PathVariable courseId:Long,@PathVariable id: Long) = service.getAll(courseId,id)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @PostMapping("/{courseId}")
    @ResponseBody
    fun create(@PathVariable courseId: Long, task: Task) = service.create(courseId,task)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, task: Task) = service.update(id, task)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)


}