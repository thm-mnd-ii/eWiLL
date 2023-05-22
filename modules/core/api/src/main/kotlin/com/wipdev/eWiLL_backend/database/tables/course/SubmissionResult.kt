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

    @Column(name = "correct", nullable = true)
    var correct: Boolean? = null

    @Column(name = "score", nullable = true)
    var score: Number? = null

    @Type(type = "jsonb")
    @Column(name = "comment", nullable = true, columnDefinition = "jsonb")
    var comments: List<Any>? = null

    @Column(name = "submissionId", nullable = false)
    var submissionId: Long? = null


    public fun addComment(comment: String) {
        comments = if (comments == null) {
            listOf(comment)
        } else {
            comments!!.plus(comment)
        }
    }

}
