package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "Model")
open class Model(entities: String?, ankerPoints: String?, id: Any?) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "entities", nullable = false)
    open var entities: String? = null

    @Column(name = "ankerPoints", nullable = false)
    open var ankerPoints: String? = null
}
