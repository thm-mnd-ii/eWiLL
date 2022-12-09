package com.wipdev.eWiLL_backend.database.tables.course

import com.wipdev.eWiLL_backend.database.tables.User
import javax.persistence.*

@Entity
@Table(name = "Course")
class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "description", nullable = false)
    open var description: String? = null

    @Column(name = "active", nullable = false)
    open var active: Boolean? = null



    @Column(name = "creationDate", nullable = false)
    open var creationDate: String? = null

    @Column(name = "startDate", nullable = false)
    open var startDate: String? = null

    @Column(name = "endDate", nullable = false)
    open var endDate: String? = null


    @JoinColumn(name = "owner_id")
    open var owner: Long? = null
}