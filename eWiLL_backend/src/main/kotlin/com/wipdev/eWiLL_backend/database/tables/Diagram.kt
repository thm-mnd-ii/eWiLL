package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "Diagram")
open class Diagram {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = true)
    open var id: Long? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "category_id", nullable = false)
    open var categoryId: Long? = null

    @Column(name = "ownerId", nullable = false)
    open var ownerId: Long? = null

    @Column(name = "entities", nullable = false, length = 100000)
    open var entities: String? = null

    @Column(name = "connections", nullable = false,length = 100000)
    open var connections: String? = null

    @Column(name = "config_id", nullable = true)
    open var configId: Long? = 0


    override fun toString(): String {
        return "Diagram(id=$id, name=$name, categoryId=$categoryId, ownerId=$ownerId, entities=$entities, connections=$connections, configId=$configId)"
    }

}
