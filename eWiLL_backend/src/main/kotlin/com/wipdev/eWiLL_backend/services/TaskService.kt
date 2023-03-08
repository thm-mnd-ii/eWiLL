package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Task
import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.TaskPL

import com.wipdev.eWiLL_backend.repository.RulesetRepository
import com.wipdev.eWiLL_backend.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskService : ITaskService {


    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var ruleSetRepository: RulesetRepository


    override fun getAll(courseId: Long): List<TaskPL> =
        taskRepository.findAll().map { convert(it) }.filter { it.courseId == courseId }


    override fun getById(id: Long): TaskPL =
        convert(taskRepository.findById(id).get())


    override fun create(courseId: Long, taskPL: TaskPL): Boolean {
        taskRepository.save(convert(taskPL))
        return true
    }

    override fun update(id: Long, taskPL: TaskPL): TaskPL {
        val assignmentEntity = convert(taskPL)
        assignmentEntity.id = id
        taskRepository.save(assignmentEntity)
        return taskPL
    }

    override fun delete(id: Long): TaskPL {
        val assignment = getById(id)
        taskRepository.deleteById(id)
        return assignment
    }

    override fun createRuleset(ruleset: Ruleset): Long? {

        return ruleSetRepository.save(ruleset).id
    }



    fun convert(task: Task): TaskPL {

        return TaskPL(
            task.name,
            task.description,
            task.dueDate,
            task.mediaType,
            task.courseId,
            task.solutionModelId,
            task.rulesetId
        )

    }


    fun convert(taskPL: TaskPL): Task {
        var task = Task()
        task.name = taskPL.name
        task.description = taskPL.description
        task.dueDate = taskPL.dueDate
        task.mediaType = taskPL.mediaType
        task.solutionModelId = taskPL.solutionModelId
        task.rulesetId = taskPL.rulesetId
        task.courseId = taskPL.courseId

        return task

    }
}
