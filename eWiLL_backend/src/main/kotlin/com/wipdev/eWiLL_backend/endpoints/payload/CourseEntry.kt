package com.wipdev.eWiLL_backend.endpoints.payload

import com.wipdev.eWiLL_backend.database.tables.course.Course

data class CourseEntry (var course: Course, var member: Boolean)
