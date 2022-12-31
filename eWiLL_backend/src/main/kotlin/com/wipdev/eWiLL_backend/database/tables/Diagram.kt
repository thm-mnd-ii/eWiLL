package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "Diagram")
open class Diagram {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "category_id", nullable = false)
    open var categoryId: Long? = null

    @Column(name = "ownerId", nullable = false)
    open var ownerId: Long? = null

    @Column(name = "entities", nullable = false)
    open var entities: String? = null

    @Column(name = "connections", nullable = false)
    open var connections: String? = null

}
