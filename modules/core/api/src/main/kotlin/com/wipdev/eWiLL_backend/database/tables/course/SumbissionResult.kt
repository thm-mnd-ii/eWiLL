package com.wipdev.eWiLL_backend.database.tables.course

import javax.persistence.*

@Table(name = "result")
@Entity
class SumbissionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "correct", nullable = true)
    open var correct: Boolean? = null

    @Column(name = "score", nullable = true)
    open var score: Number? = null

    @Column(name = "comment", nullable = true)
    open var comment: String? = null


}