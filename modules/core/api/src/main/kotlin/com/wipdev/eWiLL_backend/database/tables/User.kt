package com.wipdev.eWiLL_backend.database.tables

import javax.persistence.*


@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
open class User {

    constructor(username: String,  email: String, roles: Set<Role>) {
        this.username = username
        this.email = email
        this.roles = roles
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    open var id:Long = 0

    @Column(name = "username", nullable = false)
    open var username:String = ""


    @Column(name = "email", nullable = false)
    open var email:String = ""

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = [JoinColumn(name = "user_id")],
            inverseJoinColumns = [JoinColumn(name = "role_id")])
    open var roles: Set<Role> = hashSetOf()

    override fun toString(): String {
        return "User(id=$id, username='$username', email='$email', roles=$roles)"
    }



}