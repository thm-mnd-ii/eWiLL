package com.wipdev.eWiLL_backend.services;

import com.wipdev.eWiLL_backend.database.tables.course.Course
import com.wipdev.eWiLL_backend.repository.CourseRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class CourseService: ICourseService {

    @Autowired
    lateinit var repository: CourseRepository
    override fun getAll(): List<Course> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Course {
        TODO("Not yet implemented")
    }

    override fun create(course: Course): Course {
        TODO("Not yet implemented")
    }

    override fun update(id: Long, course: Course): Course {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getCourseByUserId(id: Long): List<Course> {
        TODO("Not yet implemented")
    }


}
