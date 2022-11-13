package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "Diagram")
open class Diagram(entities: String?, ankerPoints: String?, id: Any?) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "entities", nullable = false)
    open var entities: String? = null

    @Column(name = "ankerPoints", nullable = false)
    open var ankerPoints: String? = null

    @Column(name = "assignmentId", nullable = false)
    open var assignmentId: Long? = null
}
