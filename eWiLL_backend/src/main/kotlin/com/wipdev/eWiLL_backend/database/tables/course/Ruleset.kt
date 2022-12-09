package com.wipdev.eWiLL_backend.database.tables.course

import javax.persistence.*

@Entity
@Table(name = "ruleset")
class Ruleset(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    @Column(name = "rules", nullable = false)
    open var rules: String?//TODO: change to list of rules
) {

}