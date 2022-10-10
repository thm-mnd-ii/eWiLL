package com.wipdev.eWiLL_backend.endpoints


import com.wipdev.eWiLL_backend.endpoints.dataclasses.Model
import com.wipdev.eWiLL_backend.services.IModelService
import com.wipdev.eWiLL_backend.services.ModelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/model")
@RestController
class ModelController{

    @Autowired
    lateinit var modelService: IModelService



    @GetMapping
    fun getAll() = modelService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long) = modelService.getById(id)

    @PostMapping
    fun create(model:Model) = modelService.create(model)

    @PutMapping("/{id}")
    fun update(@PathVariable id:Long,model: Model) = modelService.update(id,model)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id:Long) = modelService.delete(id)


}