package com.wipdev.eWiLL_backend.services


import com.wipdev.eWiLL_backend.DataclassEntityConverter
import com.wipdev.eWiLL_backend.endpoints.dataclasses.Task
import com.wipdev.eWiLL_backend.repository.AssignmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskService() : ITaskService {


    @Autowired
    lateinit var assignmentRepository: AssignmentRepository


    override fun getAll(): List<Task> =
        assignmentRepository.findAll().map { DataclassEntityConverter.convert(it) }


    override fun getById(id: Long): Task =
        DataclassEntityConverter.convert(assignmentRepository.findById(id).get())


    override fun create(task: Task): Boolean {
        assignmentRepository.save(DataclassEntityConverter.convert(task))
        return true
    }

    override fun update(id: Long, task: Task): Task {
        val assignmentEntity = DataclassEntityConverter.convert(task)
        assignmentEntity.id = id
        assignmentRepository.save(assignmentEntity)
        return task
    }

    override fun delete(id: Long): Task {
        val assignment = getById(id)
        assignmentRepository.deleteById(id)
        return assignment
    }


}