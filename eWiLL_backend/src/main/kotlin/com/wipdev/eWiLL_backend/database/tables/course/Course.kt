package com.wipdev.eWiLL_backend.database.tables.course

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

    @Column(name = "description", nullable = false, length = 1000000)
    open var description: String? = null

    @Column(name = "active", nullable = false)
    open var active: Boolean? = null



    @Column(name = "creationDate", nullable = false)
    open var creationDate: String? = null


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    var semester: Semester? = null


    @Column(name = "owner_id", nullable = false)
    open var owner: Long? = null

    @Column(name="keyPassword",nullable = false)
    open var keyPassword:String? = null

    @Column(name="location",nullable = false)
    open var location:String? = null


}