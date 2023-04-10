package com.wipdev.eWiLL_backend.database.tables

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "Diagram")
data class Diagram (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long? = null,

    @Column(name = "ownerId", nullable = false)
    var ownerId: Long? = null,

    @Column(name = "entities", nullable = false, length = 100000)
    var entities: String? = null,

    @Column(name = "connections", nullable = false, length = 100000)
    var connections: String? = null,

    @Column(name = "config_id", nullable = true)
    var configId: Long? = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Diagram

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , categoryId = $categoryId , ownerId = $ownerId , entities = $entities , connections = $connections , configId = $configId )"
    }
}
