package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "diagram_config")
data class DiagramConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "diagramType", nullable = false)
    var diagramType: String? = null,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "diagram_config_element_type",
        joinColumns = [JoinColumn(name = "diagram_config_id")],
        inverseJoinColumns = [JoinColumn(name = "element_type_id")]
    )
    var elementTypes: Set<ElementType> = emptySet(),

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "diagram_config_connection_type",
        joinColumns = [JoinColumn(name = "diagram_config_id")],
        inverseJoinColumns = [JoinColumn(name = "connection_type_id")]
    )
    var connectionTypes: Set<ConnectionType> = emptySet(),

    )