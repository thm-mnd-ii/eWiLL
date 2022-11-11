package com.wipdev.eWiLL_backend.security.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<Role, Long> {

    fun findByName(name: String): Role?

}