package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.course.Course
import com.wipdev.eWiLL_backend.services.CourseService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/course")
@Tag(name = "Course", description = "Course API")
class CourseController {


    @Autowired
    lateinit var service: CourseService

    @GetMapping("/all")
    fun getAll(): List<Course>  = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Course = service.getById(id)

    @PutMapping()
    @ResponseBody
    fun create(course: Course): Course = service.create(course)

    @PostMapping("/{id}")
    @ResponseBody
    fun update(@PathVariable id: Long, course: Course): Course = service.update(id, course)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
    @GetMapping()
    @ResponseBody
    fun getCourseByUserId(id: Long): List<Course> {
        TODO("Not yet implemented")
    }


}