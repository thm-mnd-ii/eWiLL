package com.wipdev.eWiLL_backend.services


import com.wipdev.eWiLL_backend.DataclassEntityConverter
import com.wipdev.eWiLL_backend.endpoints.dataclasses.Assignment
import com.wipdev.eWiLL_backend.repository.AssignmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AssignmentService() : IAssignmentService {


    @Autowired
    lateinit var assignmentRepository: AssignmentRepository


    override fun getAll(): List<Assignment> =
        assignmentRepository.findAll().map { DataclassEntityConverter.convert(it) }


    override fun getById(id: Long): Assignment =
        DataclassEntityConverter.convert(assignmentRepository.findById(id).get())


    override fun create(assignment: Assignment): Boolean {
        assignmentRepository.save(DataclassEntityConverter.convert(assignment))
        return true
    }

    override fun update(id: Long, assignment: Assignment): Assignment {
        val assignmentEntity = DataclassEntityConverter.convert(assignment)
        assignmentEntity.id = id
        assignmentRepository.save(assignmentEntity)
        return assignment
    }

    override fun delete(id: Long): Assignment {
        val assignment = getById(id)
        assignmentRepository.deleteById(id)
        return assignment
    }


}