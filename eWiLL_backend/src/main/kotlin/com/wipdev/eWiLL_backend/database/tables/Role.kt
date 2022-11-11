package com.wipdev.eWiLL_backend.database.tables

import com.wipdev.eWiLL_backend.security.auth.ERole
import javax.persistence.*

@Entity
@Table(name = "roles")
class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null


    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    open var name: ERole? = ERole.ROLE_USER


}