package com.wipdev.eWiLL_backend.services.serviceInterfaces

import com.wipdev.eWiLL_backend.database.tables.Category
import com.wipdev.eWiLL_backend.endpoints.payload.requests.CategoryPL
import org.springframework.stereotype.Service

@Service
interface ICategoryService {

    fun createCategory(category: CategoryPL): Category
    fun deleteCategory(id: Long): Category
    fun getByUserId(user_id: Long): List<Category>
    fun updateCategory(id: Long, category: CategoryPL): Category
}