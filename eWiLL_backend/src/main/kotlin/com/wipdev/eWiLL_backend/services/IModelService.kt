package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.endpoints.dataclasses.Model
import org.springframework.stereotype.Service

@Service
interface IModelService {
    fun getAll(): List<Model>
    fun getById(id: Long): Model
    fun create(model: Model): Model
    fun update(id: Long, model: Model): Model
    fun delete(id: Long): Model
}