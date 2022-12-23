package com.wipdev.eWiLL_backend.endpoints.payload.requests

data class TaskPL(var id: Long?, val name: String?, val description: String?, val dueDate: String?, val subject: String?, val courseId:Long?, val solutionModelId: Long?, val rulesetId:Long?)