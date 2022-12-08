package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.Task
import com.wipdev.eWiLL_backend.services.TaskService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/tasks")
@RestController
@Tag(name = "Task", description = "Task API")
class TaskController {

    @Autowired
    lateinit var service: TaskService

    @GetMapping("/{courseId}/tasks", produces = ["application/json"])
    fun getAll(@PathVariable courseId:Long) = service.getAll(courseId)

    @GetMapping("/{id}")
    @ResponseBody
    fun getById(@PathVariable id: Long) = service.getById(id)

    @PostMapping("/{courseId}")
    @ResponseBody
    fun create(@PathVariable courseId: Long,@Parameter task: Task) = service.create(courseId,task)

    @PutMapping()
    @ResponseBody
    fun update(@Parameter task: Task) = task.id?.let { service.update(it,task) }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)

    @PostMapping("/ruleset")
    fun createRuleset(@Parameter ruleset: Ruleset) = service.createRuleset(ruleset)


}