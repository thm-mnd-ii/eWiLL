package com.wipdev.eWiLL_backend.endpoints.payload.requests

data class Connection(val startEntity: Long?, val startEntityPosition: String?, val endEntity: Long?, val endEntityPosition: String?, val style: CardinalityType?)
