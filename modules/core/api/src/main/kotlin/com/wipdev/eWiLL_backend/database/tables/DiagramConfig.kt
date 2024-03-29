package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "diagram_config")
class DiagramConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "diagram_type", nullable = false)
    var diagramtype: String? = null
}