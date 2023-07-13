package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.Category
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : CrudRepository<Category, Long> {

    fun findAllByUserId(user_id: Long?): List<Category>


}