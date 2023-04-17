package com.wipdev.eWiLL_backend.database.tables.course

import javax.persistence.*

@Entity
@Table(name = "Course")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "description", nullable = false, length = 1000000)
    var description: String? = null,

    @Column(name = "active", nullable = false)
    var active: Boolean? = null,

    @Column(name = "creationDate", nullable = false)
    var creationDate: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    var semester: Semester? = null,

    @Column(name = "owner_id", nullable = false)
    var owner: Long? = null,

    @Column(name = "keyPassword", nullable = false)
    var keyPassword: String? = null,

    @Column(name = "location", nullable = false)
    var location: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Course

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Course(id=$id, name=$name, description=$description, active=$active, creationDate=$creationDate, semester=$semester, owner=$owner, keyPassword=$keyPassword, location=$location)"
    }
}
