package com.wipdev.eWiLL_backend.endpoints.payload

import com.wipdev.eWiLL_backend.database.tables.User
import com.wipdev.eWiLL_backend.database.tables.course.ECourseRole

data class CourseUser(val user: User, val role: ECourseRole)
