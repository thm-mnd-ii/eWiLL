package com.wipdev.eWiLL_backend.database.tables.course

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

    @Column(name = "comment", nullable = true)
    var comment: String? = null

    @Column(name = "submissionId", nullable = false)
    var submissionId: Long? = null

    override fun toString(): String {
        return "SumbissionResult(id=$id, correct=$correct, score=$score, comment=$comment)"
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (correct?.hashCode() ?: 0)
        result = 31 * result + (score?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SubmissionResult

        if (id != other.id) return false
        if (correct != other.correct) return false
        if (score != other.score) return false
        return comment == other.comment
    }
}
