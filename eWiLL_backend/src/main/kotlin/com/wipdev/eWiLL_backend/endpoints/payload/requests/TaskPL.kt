package com.wipdev.eWiLL_backend.endpoints.payload.requests

data class TaskPL(val name: String?, val description: String?, val dueDate: String?, val mediaType: String?,val courseId:Long?, val solutionModelId: Long?, val rulesetId:Long?)