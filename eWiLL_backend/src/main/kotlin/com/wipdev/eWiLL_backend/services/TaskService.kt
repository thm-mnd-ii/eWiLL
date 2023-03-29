package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Task
import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.TaskPL
import com.wipdev.eWiLL_backend.repository.DiagramRepository

import com.wipdev.eWiLL_backend.repository.RulesetRepository
import com.wipdev.eWiLL_backend.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskService : ITaskService {


    @Autowired
    lateinit var taskRepository: TaskRepository


    @Autowired
    lateinit var diagramService: DiagramService

    @Autowired
    lateinit var ruleSetRepository: RulesetRepository


    override fun getAll(courseId: Long): List<TaskPL> =
        taskRepository.findAllByCourseId(courseId).map { convert(it) }


    override fun getById(id: Long): TaskPL {
        return convert(taskRepository.findById(id).get())
    }


    override fun create(courseId: Long, taskPL: TaskPL): Task {
        var solutionDiagramId = diagramService.create(taskPL.solutionModel!!)
        return  taskRepository.save(convert(taskPL,solutionDiagramId))
    }

    override fun update(id: Long, taskPL: TaskPL): TaskPL {
        val newTask = convert(taskPL,taskPL.solutionModel?.id)
        val assignmentEntity = taskRepository.findById(id).get()
        newTask.id = assignmentEntity.id
        if(taskPL.solutionModel?.id != null){
            diagramService.update(taskPL.solutionModel?.id!!,taskPL.solutionModel!!)
        }

        taskRepository.save(newTask)
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
            diagramService.getById(task.solutionModelId!!),
            task.rulesetId,
            task.liability
        )

    }


    fun convert(taskPL: TaskPL,solutionModelId : Long?): Task {
        var task = Task()
        task.name = taskPL.name
        task.description = taskPL.description
        task.dueDate = taskPL.dueDate
        task.mediaType = taskPL.mediaType
        task.solutionModelId = solutionModelId
        task.rulesetId = taskPL.rulesetId
        task.courseId = taskPL.courseId
        task.liability = taskPL.liability

        return task

    }
}
