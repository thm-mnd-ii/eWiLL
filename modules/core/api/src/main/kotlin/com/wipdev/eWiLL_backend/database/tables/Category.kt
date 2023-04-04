package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "Category")
class Category {
    constructor(name: String?, userId: Long?) {
        this.name = name
        this.userId = userId
    }
    //Name, id, userid PrimaryKey(id,userid)

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null

}