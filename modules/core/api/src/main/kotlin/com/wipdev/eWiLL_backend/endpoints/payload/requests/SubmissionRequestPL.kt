package com.wipdev.eWiLL_backend.endpoints.payload.requests

import com.wipdev.eWiLL_backend.database.tables.course.Submission

data class SubmissionRequestPL(val diagramPL:DiagramPL, val taskId:Long) {

}
