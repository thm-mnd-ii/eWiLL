package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.StatusLevel
import com.wipdev.eWiLL_backend.eval.FeedbackLevel
import io.swagger.v3.core.util.Json


class ResultMessage() {
    var feedbackLevel: FeedbackLevel = FeedbackLevel.BASIC
    var message: String? = null
    var resultMessageType = ResultMessageType.Entity
    var affectedEntityId: Long = -1
    var affectedAttributeName: String = ""
    var connectedToId = -1L
    var statusLevel: StatusLevel = StatusLevel.NOTHING




    constructor(
        feedbackLevel: FeedbackLevel,
        message: String?,
        affectedEntityId: Long,
        affectedAttributeName: String,
        statusLevel: StatusLevel,
        resultMessageType: ResultMessageType
    ) : this() {
        this.feedbackLevel = feedbackLevel
        this.message = message
        this.affectedEntityId = affectedEntityId
        this.affectedAttributeName = affectedAttributeName
        this.statusLevel = statusLevel
        this.resultMessageType = resultMessageType
    }
    constructor(
        feedbackLevel: FeedbackLevel,
        message: String?,
        affectedEntityId: Long,
        affectedAttributeName: String,
        statusLevel: StatusLevel,
        resultMessageType: ResultMessageType,
        connectedToId: Long
    ) : this() {
        this.feedbackLevel = feedbackLevel
        this.message = message
        this.affectedEntityId = affectedEntityId
        this.affectedAttributeName = affectedAttributeName
        this.statusLevel = statusLevel
        this.resultMessageType = resultMessageType
        this.connectedToId = connectedToId
    }

    override fun toString(): String {
        return "ResultMessage(resultLevel=$feedbackLevel, message=$message, affectedEntityId=$affectedEntityId, affectedAttributeName='$affectedAttributeName', highlightLevel=$statusLevel, resultMessageType=$resultMessageType)"
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