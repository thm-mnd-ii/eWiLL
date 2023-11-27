package com.wipdev.eWiLL_backend.database.tables

import org.hibernate.Hibernate
import javax.persistence.*


@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
data class User(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = 0,

    @Column(name = "username", nullable = false)
    val username: String = "",


    @Column(name = "email", nullable = false)
    val email: String = "",

    @Column(name = "firstName", nullable = true)
    val firstName: String = "",

    @Column(name = "lastName", nullable = true)
    val lastName: String = "",

    @ManyToMany(fetch = FetchType.EAGER) @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    ) val roles: Set<Role> = emptySet()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , username = $username , email = $email )"
    }
}


