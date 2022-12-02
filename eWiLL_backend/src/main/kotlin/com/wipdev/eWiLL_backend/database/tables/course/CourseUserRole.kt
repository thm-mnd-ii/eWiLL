package com.wipdev.eWiLL_backend.database.tables.course

import com.wipdev.eWiLL_backend.database.tables.User
import javax.persistence.*

@Entity
@Table(name = "course_user_role")
class CourseUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null


    @Column(name = "courseId", nullable = false)
    open var courseId: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    open var role: ECourseRole? = ECourseRole.STUDENT

    @ManyToOne
    @JoinColumn(name = "user_id")
    open var user: User? = null
}