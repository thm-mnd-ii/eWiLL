package com.wipdev.eWiLL_backend.endpoints.payload.requests

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset

data class Task(val id: Long?, val name: String?, val description: String?, val dueDate: String?, val subject: String?, val courseId:Long?,val solutionModelId: Long?,val rulesetId:Long?){



}