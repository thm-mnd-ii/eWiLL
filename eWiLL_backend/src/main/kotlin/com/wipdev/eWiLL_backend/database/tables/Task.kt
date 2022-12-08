package com.wipdev.eWiLL_backend.database.tables

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import javax.persistence.*

@Entity
@Table(name = "Assignment")
class Task {
    constructor(id: Long?, name: String?, description: String?, dueDate: String?, subject: String?, courseId: Long?, solutionModelId: Long?, rulesetId: Long?)
    {
        this.id = id
        this.name = name
        this.description = description
        this.dueDate = dueDate
        this.subject = subject
        this.courseId = courseId
        this.solutionModelId = solutionModelId
        this.rulesetId = rulesetId
    }


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

    @Column(name = "courseId", nullable = false)
    var courseId: Long? = null

    @Column(name="solutionModelId", nullable = true)
    var solutionModelId:Long?=null

    @Column(name="rulesetId", nullable = false)
    var rulesetId:Long?=null

}
