package com.wipdev.eWiLL_backend.endpoints.payload.requests

data class JoinRequestPL(
    var keyPass: String,
    val userId: Long
)
