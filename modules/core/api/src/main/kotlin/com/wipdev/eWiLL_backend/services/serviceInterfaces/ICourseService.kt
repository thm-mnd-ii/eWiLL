package com.wipdev.eWiLL_backend.services.serviceInterfaces

import com.wipdev.eWiLL_backend.database.tables.course.Course
import com.wipdev.eWiLL_backend.database.tables.course.CourseUserRole
import com.wipdev.eWiLL_backend.database.tables.course.ECourseRole
import com.wipdev.eWiLL_backend.endpoints.payload.CourseEntry
import com.wipdev.eWiLL_backend.endpoints.payload.CourseUser

interface ICourseService {
    fun getAll(userId:Long): List<CourseEntry>
    fun getById(id: Long): Course
    fun create(course: Course): Course
    fun update(id: Long, course: Course): Course
    fun delete(id: Long)

    fun getCourseByUserId(id: Long): List<Course>
    fun getStudentsByCourseId(id: Long): List<CourseUser>
    fun joinCourse(id: Long, keyPass: String, userId: Long): CourseUserRole
    fun leaveCourse(id: Long, userId: Long): Course
    fun hasKeyPass(id: Long): Boolean
    fun removeAllButOwner(id: Long): Course
    fun archiveCourse(id: Long): Course
    fun changeUserRole(id: Long, role: ECourseRole): CourseUserRole
    fun getUserRoleInCourse(courseId: Long, userId: Long): ECourseRole?


}