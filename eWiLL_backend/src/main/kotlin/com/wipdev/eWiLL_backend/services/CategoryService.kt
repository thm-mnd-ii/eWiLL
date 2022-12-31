package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.Category
import com.wipdev.eWiLL_backend.endpoints.payload.requests.CategoryPL
import com.wipdev.eWiLL_backend.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService : ICategoryService{

    @Autowired
    lateinit var categoryRepository: CategoryRepository


    override fun createCategory(category: CategoryPL): Category {
        return categoryRepository.save(Category(category.name,category.userid))
    }

    override fun deleteCategory(id: Long): Category {
        val category = categoryRepository.findById(id).get()
        categoryRepository.delete(category)
        return category
    }

    override fun getByUserId(user_id: Long): List<Category> {
        return categoryRepository.findAllByUserId(user_id)
    }

}
