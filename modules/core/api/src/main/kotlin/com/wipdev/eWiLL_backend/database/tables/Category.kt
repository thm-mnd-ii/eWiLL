package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*

@Entity
@Table(name = "Category")
data class Category(
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Category

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "${this::class.simpleName}(id=$id, name=$name, userId=$userId)"
    }
}