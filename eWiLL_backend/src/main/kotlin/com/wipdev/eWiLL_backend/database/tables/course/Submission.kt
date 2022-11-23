package com.wipdev.eWiLL_backend.database.tables.course

import com.wipdev.eWiLL_backend.database.tables.Diagram
import javax.persistence.*

@Entity
@Table(name = "submission")
class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null
//TODO: add fields
}