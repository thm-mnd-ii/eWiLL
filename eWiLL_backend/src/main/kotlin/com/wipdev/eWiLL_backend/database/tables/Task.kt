package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "Assignment")
class Task(id: Long?, name: String?, description: String?, dueDate: String?, subject: String?, teacher: String?) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "description", nullable = false)
    var description: String? = null

    @Column(name = "dueDate", nullable = false)
    var dueDate: String? = null

    @Column(name = "subject", nullable = false)
    var subject: String? = null

    @Column(name = "teacher", nullable = false)
    var teacher: String? = null

    @Column(name="solutionModelId", nullable = true)
    var solutionModelId:Long?=null

}
