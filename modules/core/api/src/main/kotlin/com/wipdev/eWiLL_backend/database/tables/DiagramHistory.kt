package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity(name = "DiagramHistory")
data class DiagramHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = true)
    var id: Long?,

    @Column(name = "diagram_id", nullable = false)
    var diagramId: Long?,

    @Column(name = "timeStamp", nullable = false)
    var timeStamp: String?,

    @Column(name = "entities", nullable = false, length = 100000)
    var entities: String? = null,

    @Column(name = "connections", nullable = false, length = 100000)
    var connections: String? = null,

    @Column(name = "saveType", nullable = true)
    var saveType :String? = ""

)