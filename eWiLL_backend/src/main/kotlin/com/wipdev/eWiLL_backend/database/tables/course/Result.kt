package com.wipdev.eWiLL_backend.database.tables.course

import javax.persistence.*

@Table(name = "result")
@Entity
class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "correct", nullable = false)
    open var correct: Boolean? = null

    @Column(name = "score", nullable = false)
    open var score: Number? = null

    @Column(name = "comment", nullable = false)
    open var comment: String? = null

}