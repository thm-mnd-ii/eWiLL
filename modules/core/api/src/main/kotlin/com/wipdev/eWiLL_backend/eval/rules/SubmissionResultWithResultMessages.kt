package com.wipdev.eWiLL_backend.eval.rules

data class SubmissionResultWithResultMessages(
    var id: Long? = null,

    var correct: Boolean? = false,

    var score: Float? = 0f,

    var comments: MutableList<ResultMessage> = mutableListOf(),

    var submissionId: Long? = null
)
