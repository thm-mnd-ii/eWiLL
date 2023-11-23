package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Task
import com.wipdev.eWiLL_backend.endpoints.payload.requests.TaskPL
import com.wipdev.eWiLL_backend.repository.TaskRepository
import com.wipdev.eWiLL_backend.services.serviceInterfaces.ITaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskService : ITaskService {

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var diagramService: DiagramService

    override fun getAll(courseId: Long): List<Task> {
        return taskRepository.findAllByCourseId(courseId)
    }

    override fun getById(id: Long): Task {
        return taskRepository.findById(id).get()
    }

    override fun create(courseId: Long, task: Task): Task {
        task.id = null
        val model = diagramService.getById(task.solutionModelId!!)
        model.id = null
        model.name = task.name + " - solution model"
        model.ownerId = -1
        val newDiagramId = diagramService.create(model)
        task.solutionModelId = newDiagramId
        return taskRepository.save(task)
    }

    override fun update(id: Long, task: Task): Task {
        val assignmentEntity = taskRepository.findById(id).get()
        task.id = assignmentEntity.id
        if (task.solutionModelId != null && task.solutionModelId != assignmentEntity.solutionModelId
        ) {
            val model = diagramService.getById(task.solutionModelId!!)
            model.id = null
            model.ownerId = -1
            model.name += "(Kopie)"

            val newDiagramId = diagramService.create(model)
            task.solutionModelId = newDiagramId
        }
        taskRepository.save(task)
        return task
    }

    override fun delete(id: Long): Task {
        val assignment = getById(id)
        taskRepository.deleteById(id)
        return assignment
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
            task.eLiability,
            task.showLevel,
            task.taskLevel,
        )
    }

    fun convert(taskPL: TaskPL, solutionModelId: Long?): Task {
        val task = Task()
        task.name = taskPL.name
        task.description = taskPL.description
        task.dueDate = taskPL.dueDate
        task.mediaType = taskPL.mediaType
        task.solutionModelId = solutionModelId
        task.rulesetId = taskPL.rulesetId
        task.courseId = taskPL.courseId
        task.eLiability = taskPL.ELiability
        task.showLevel = taskPL.showLevel
        task.taskLevel = taskPL.taskLevel

        return task
    }
}
