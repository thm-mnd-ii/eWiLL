package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.services.EvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/evaluation/v1")
@Controller
class EvaluationController {
    @Autowired
    lateinit var service: EvaluationService

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long) = service.get(id)


}