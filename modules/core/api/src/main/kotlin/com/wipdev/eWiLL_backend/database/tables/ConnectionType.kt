package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*
@Entity
@Table(name = "connection_type")
data class ConnectionType (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "icon", nullable = false)
    var icon: String? = null,

    @Column(name = "color", nullable = false)
    var color: String? = null,
)