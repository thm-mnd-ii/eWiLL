package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long>