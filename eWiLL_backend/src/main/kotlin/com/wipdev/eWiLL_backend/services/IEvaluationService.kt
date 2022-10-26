package com.wipdev.eWiLL_backend.services

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
interface IEvaluationService {

    fun get(id: Long): Int;
}