package com.wipdev.eWiLL_backend.database.tables.course

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
    @Column(name = "comment", nullable = true, columnDefinition = "jsonb")
    var comments: List<Any>? = null

    @Column(name = "submissionId", nullable = false)
    var submissionId: Long? = null


    fun addComment(comment: String) {
        comments = if (comments == null) {
            listOf(comment)
        } else {
            comments!!.plus(comment)
        }
    }

}
