package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.endpoints.dataclasses.Assignment
import org.springframework.stereotype.Service

@Service
interface IAssignmentService {

    fun getAll() : List<Assignment>
    fun getById(id: Long): Assignment
    fun create(assignment: Assignment): Boolean
    fun update(id: Long, assignment: Assignment): Assignment
    fun delete(id: Long): Assignment

}