package com.wipdev.eWiLL_backend.database.tables.course

import com.wipdev.eWiLL_backend.eval.rules.ResultMessage
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "result")
class SubmissionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "correct", nullable = false)
    var correct: Boolean? = false

    @Column(name = "score", nullable = false)
    var score: Float? = 0f

    @Type(type = "com.wipdev.eWiLL_backend.database.tables.utils.JsonbUserType")
    @Column(name = "comment", nullable = true, columnDefinition = "jsonb", length = 100000)
    private var comments: List<Any>? = null


    @Column(name = "submissionId", nullable = false)
    var submissionId: Long? = null


    fun getResultMessages(): List<ResultMessage> {
        return comments!!.map { ResultMessage.fromJsonString(it.toString()) }
    }

    fun addResultMessage(message: ResultMessage) {
        comments = if (comments == null) {
            listOf(message.toJsonString())
        } else {
            comments!!.plus(message.toJsonString())
        }
    }

}
