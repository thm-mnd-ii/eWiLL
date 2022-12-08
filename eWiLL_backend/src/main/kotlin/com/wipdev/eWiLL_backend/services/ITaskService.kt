package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.Task
import org.springframework.stereotype.Service

@Service
interface ITaskService {

    fun getAll(courseId: Long): List<Task>
    fun getById(id: Long): Task
    fun create(courseId: Long, task: Task): Boolean
    fun update(id: Long, task: Task): Task
    fun delete(id: Long): Task
    fun createRuleset(ruleset: Ruleset): Long?

}