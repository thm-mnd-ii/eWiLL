package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.Category
import com.wipdev.eWiLL_backend.endpoints.payload.requests.CategoryPL
import com.wipdev.eWiLL_backend.services.CategoryService
import com.wipdev.eWiLL_backend.services.DiagramService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/category")
@RestController
class CategoryController {

    @Autowired
    lateinit var iCategoryService: CategoryService

    @Autowired
    lateinit var iDiagramService: DiagramService


    @GetMapping("/{id}/diagrams")
    fun getById(@PathVariable id: Long) = iDiagramService.getAllByCategoryId(id)


    @PostMapping
    @ResponseBody
    fun createCategory(@RequestBody category: CategoryPL): Category {
        return iCategoryService.createCategory(category)
    }


    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): Category {
        return iCategoryService.deleteCategory(id)
    }


    @GetMapping("/user/{user_id}")
    fun getByUserId(@PathVariable user_id: Long): List<Category> {
        return iCategoryService.getByUserId(user_id)
    }


    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Long, @RequestBody category: CategoryPL): Category {
        return iCategoryService.updateCategory(id, category)
    }
}