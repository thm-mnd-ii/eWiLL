package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "diagram_config")
class DiagramConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null
    //
    @Column(name ="rulesetId", nullable = false)
    open var rulesetId: Long? = null
}