package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.DataclassEntityConverter
import com.wipdev.eWiLL_backend.endpoints.dataclasses.Model
import com.wipdev.eWiLL_backend.repository.ModelRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ModelService (): IModelService{

    @Autowired
    private lateinit var modelRepository: ModelRepository

    override fun getAll(): List<Model> {
        LoggerFactory.getLogger(ModelService::class.java)
        return modelRepository.findAll().map { DataclassEntityConverter.convert(it) }
    }

    override fun getById(id: Long): Model {


        return DataclassEntityConverter.convert(modelRepository.findById(id).get())
    }

    override fun create(model: Model): Model {
        return DataclassEntityConverter.convert(modelRepository.save(DataclassEntityConverter.convert(model)))
    }

    override fun update(id: Long, model: Model): Model {
        return DataclassEntityConverter.convert(modelRepository.save(DataclassEntityConverter.convert(model)))
    }

    override fun delete(id: Long): Model {
        val model = modelRepository.findById(id).get()
        modelRepository.delete(model)
        return DataclassEntityConverter.convert(model)
    }
}