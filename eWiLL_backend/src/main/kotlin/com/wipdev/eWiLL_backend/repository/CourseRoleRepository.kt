package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.CourseUserRole
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface CourseRoleRepository : CrudRepository<CourseUserRole, Long> {

    @Query("select (count(c) > 0) from CourseUserRole c")
    fun existsByCourseIdAndUserId(courseId: Long, userId: Long): Boolean

    @Transactional
    @Modifying
    @Query("delete from CourseUserRole c")
    fun deleteByCourseIdAndUserId(id: Long, userId: Long): Int


}