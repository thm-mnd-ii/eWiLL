package com.wipdev.eWiLL_backend.database.tables

import com.wipdev.eWiLL_backend.MediaType
import javax.persistence.*

@Entity
@Table(name = "Task")
class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "description", nullable = false, length = 100000)
    var description: String? = null

    @Column(name = "dueDate", nullable = false)
    var dueDate: String? = null

    @Column(name="mediatype", nullable = false)
    var mediaType: MediaType? = null

    @Column(name="liability")
    var liability: Liability? = Liability.NONE

    @Column(name = "courseId", nullable = false)
    var courseId: Long? = null

    @Column(name="solutionModelId", nullable = true)
    var solutionModelId:Long?=null

    @Column(name="rulesetId", nullable = false)
    var rulesetId:Long?=null

}
