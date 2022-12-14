package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.DataclassEntityConverter
import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.Task

import com.wipdev.eWiLL_backend.repository.RulesetRepository
import com.wipdev.eWiLL_backend.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskService() : ITaskService {


    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var ruleSetRepository: RulesetRepository


    override fun getAll(courseId: Long): List<Task> =
        taskRepository.findAll().map { DataclassEntityConverter.convert(it) }.filter { it.courseId == courseId }


    override fun getById(id: Long): Task =
        DataclassEntityConverter.convert(taskRepository.findById(id).get())


    override fun create(courseId: Long, task: Task): Boolean {
        taskRepository.save(DataclassEntityConverter.convert(task))
        return true
    }

    override fun update(id: Long, task: Task): Task {
        val assignmentEntity = DataclassEntityConverter.convert(task)
        assignmentEntity.id = id
        taskRepository.save(assignmentEntity)
        return task
    }

    override fun delete(id: Long): Task {
        val assignment = getById(id)
        taskRepository.deleteById(id)
        return assignment
    }

    override fun createRuleset(ruleset: Ruleset): Long? {

        return ruleSetRepository.save(ruleset).id
    }


}
