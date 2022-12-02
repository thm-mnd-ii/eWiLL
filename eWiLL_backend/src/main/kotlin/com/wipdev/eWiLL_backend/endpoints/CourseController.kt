package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.course.Course
import com.wipdev.eWiLL_backend.services.CourseService
import com.wipdev.eWiLL_backend.services.ICourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/course")
class CourseController :ICourseService{


    @Autowired
    lateinit var service: CourseService

    @GetMapping("/all")
    override fun getAll(): List<Course>  = service.getAll()

    @GetMapping("/{id}")
    override fun getById(@PathVariable id: Long): Course = service.getById(id)

    @PutMapping()
    override fun create(course: Course): Course = service.create(course)

    @PostMapping("/{id}")
    override fun update(@PathVariable id: Long, course: Course): Course = service.update(id, course)

    @DeleteMapping("/{id}")
    override fun delete(@PathVariable id: Long) = service.delete(id)




}