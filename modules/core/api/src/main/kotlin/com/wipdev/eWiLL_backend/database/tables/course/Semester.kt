package com.wipdev.eWiLL_backend.database.tables.course

import javax.persistence.*

@Entity
@Table(name = "semester")
data class Semester(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "start_date", nullable = false)
    var startDate: String? = null,

    @Column(name = "end_date", nullable = false)
    var endDate: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Semester

        if (id != other.id) return false
        if (name != other.name) return false
        if (startDate != other.startDate) return false
        return endDate == other.endDate
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (startDate?.hashCode() ?: 0)
        result = 31 * result + (endDate?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Semester(id=$id, name=$name, startDate=$startDate, endDate=$endDate)"
    }
}
