package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository:CrudRepository<Course,Long> {


}