package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.CourseUserRole
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface CourseRoleRepository : CrudRepository<CourseUserRole, Long> {

    @Query("select (count(c) > 0) from CourseUserRole c where c.courseId = :courseId and c.userId = :userId")
    fun existsByCourseIdAndUserId(courseId: Long, userId: Long): Boolean

    @Transactional
    @Modifying
    @Query("delete from CourseUserRole c where c.courseId = :id and c.userId = :userId")
    fun deleteByCourseIdAndUserId(id: Long, userId: Long): Int


    @Query("select c from CourseUserRole c where c.courseId = :courseId and c.userId = :userId")
    fun findByCourseIdAndUserId(courseId: Long, userId: Long): CourseUserRole?

    @Modifying
    @Query("update CourseUserRole c set c.role = :role where c.courseId = :courseId and c.userId = :userId")
    fun updateByCourseIdAndUserId(courseId: Long, userId: Long, role: String): Int
}