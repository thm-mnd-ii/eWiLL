package com.wipdev.eWiLL_backend.database.tables.course

import javax.persistence.*

@Entity
@Table(name = "submission")
class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "userId", nullable = false)
    var userId: Long? = null

    @Column(name = "taskId", nullable = false)
    var taskId: Long? = null

    @Column(name = "date", nullable = false)
    var date: String? = null

    @Column(name = "diagram", nullable = false, length = 100000)
    var diagram: String? = null

    @Column(name = "attempt", nullable = false)
    var attempt: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Submission

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Submission(id=$id, userId=$userId, taskId=$taskId, date=$date, diagram=$diagram)"
    }
}
