package com.wipdev.eWiLL_backend.database.tables.course

import com.wipdev.eWiLL_backend.database.tables.Diagram
import javax.persistence.*

@Entity
@Table(name = "submission")
class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "userId", nullable = false)
    open var userId: Long? = null


    @Column(name = "taskId", nullable = false)
    open var taskId: Long? = null

    @Column(name = "date", nullable = false)
    open var date: String? = null

    @Column(name = "diagram",nullable = false)
    open var diagram: String? = null
}