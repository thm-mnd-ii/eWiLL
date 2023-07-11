package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.HighlightLevel
import com.wipdev.eWiLL_backend.eval.ResultLevel
import io.swagger.v3.core.util.Json


class ResultMessage() {
    var resultLevel: ResultLevel = ResultLevel.BASIC
    var message: String? = null
    var resultMessageType = ResultMessageType.Entity
    var affectedEntityId: Long = -1
    var affectedAttributeName: String = ""
    var connectedToId = -1L
    var highlightLevel: HighlightLevel = HighlightLevel.NOTHING




    constructor(
        resultLevel: ResultLevel,
        message: String?,
        affectedEntityId: Long,
        affectedAttributeName: String,
        highlightLevel: HighlightLevel,
        resultMessageType: ResultMessageType
    ) : this() {
        this.resultLevel = resultLevel
        this.message = message
        this.affectedEntityId = affectedEntityId
        this.affectedAttributeName = affectedAttributeName
        this.highlightLevel = highlightLevel
        this.resultMessageType = resultMessageType
    }
    constructor(
        resultLevel: ResultLevel,
        message: String?,
        affectedEntityId: Long,
        affectedAttributeName: String,
        highlightLevel: HighlightLevel,
        resultMessageType: ResultMessageType,
        connectedToId: Long
    ) : this() {
        this.resultLevel = resultLevel
        this.message = message
        this.affectedEntityId = affectedEntityId
        this.affectedAttributeName = affectedAttributeName
        this.highlightLevel = highlightLevel
        this.resultMessageType = resultMessageType
        this.connectedToId = connectedToId
    }

    override fun toString(): String {
        return "ResultMessage(resultLevel=$resultLevel, message=$message, affectedEntityId=$affectedEntityId, affectedAttributeName='$affectedAttributeName', highlightLevel=$highlightLevel, resultMessageType=$resultMessageType)"
    }

    fun toJsonString(): String {
        return Json.mapper().writeValueAsString(this)
    }

    companion object {
        fun fromJsonString(toString: String): ResultMessage {
            return Json.mapper().readValue(toString, ResultMessage::class.java)
        }
    }
}