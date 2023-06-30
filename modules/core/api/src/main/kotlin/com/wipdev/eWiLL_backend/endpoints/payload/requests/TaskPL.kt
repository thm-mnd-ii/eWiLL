package com.wipdev.eWiLL_backend.endpoints.payload.requests

import com.wipdev.eWiLL_backend.MediaType
import com.wipdev.eWiLL_backend.database.tables.ELiability
import com.wipdev.eWiLL_backend.eval.ResultLevel

data class TaskPL(val name: String?, val description: String?, val dueDate: String?, val mediaType: MediaType?, val courseId:Long?, val solutionModel: DiagramPL?, val rulesetId:Long?, var ELiability: ELiability?, var showLevel: ResultLevel? = ResultLevel.BASIC)