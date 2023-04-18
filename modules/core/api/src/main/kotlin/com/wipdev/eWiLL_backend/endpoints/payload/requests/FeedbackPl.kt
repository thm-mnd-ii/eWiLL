package com.wipdev.eWiLL_backend.endpoints.payload.requests

import com.wipdev.eWiLL_backend.database.tables.EFeedbackStatus

data class FeedbackPl(val firstName:String?,val lastName:String?,val text:String,val status:EFeedbackStatus?)
