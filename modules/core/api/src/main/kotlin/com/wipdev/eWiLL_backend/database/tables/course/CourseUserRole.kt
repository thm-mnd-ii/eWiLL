package com.wipdev.eWiLL_backend.database.tables.course

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "course_user_role")
data class CourseUserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "courseId", nullable = false)
    var courseId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    var role: ECourseRole? = ECourseRole.STUDENT,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CourseUserRole

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , courseId = $courseId , role = $role , userId = $userId )"
    }
}
