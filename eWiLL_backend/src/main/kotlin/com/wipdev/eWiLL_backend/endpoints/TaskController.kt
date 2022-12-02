package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.endpoints.payload.Task
import com.wipdev.eWiLL_backend.services.TaskService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/tasks")
@RestController
@Tag(name = "Task", description = "Task API")
class TaskController {

    @Autowired
    lateinit var service: TaskService

    @Operation(summary = "Get all Tasks")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the Tasks"),
        ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
    ])
    @GetMapping("/{courseId}", produces = ["application/json"])
    fun getAll(@Parameter(name = "Course Id to see all tasks off")@PathVariable courseId:Long) = service.getAll(courseId)

    @GetMapping("/{id}")
    @ResponseBody
    fun getById(@PathVariable id: Long) = service.getById(id)

    @PostMapping("/{courseId}")
    @ResponseBody
    fun create(@PathVariable courseId: Long, task: Task) = service.create(courseId,task)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, task: Task) = service.update(id, task)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)


}