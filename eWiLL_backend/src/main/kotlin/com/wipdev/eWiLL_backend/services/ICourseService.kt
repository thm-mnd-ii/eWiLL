package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Course

interface ICourseService {
    fun getAll(): List<Course>
    fun getById(id: Long): Course
    fun create(course: Course): Course
    fun update(id: Long, course: Course): Course
    fun delete(id: Long)
}