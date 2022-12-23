package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.TaskPL

import org.springframework.stereotype.Service

@Service
interface ITaskService {

    fun getAll(courseId: Long): List<TaskPL>
    fun getById(id: Long): TaskPL
    fun create(courseId: Long, taskPL: TaskPL): Boolean
    fun update(id: Long, taskPL: TaskPL): TaskPL
    fun delete(id: Long): TaskPL
    fun createRuleset(ruleset: Ruleset): Long?

}
