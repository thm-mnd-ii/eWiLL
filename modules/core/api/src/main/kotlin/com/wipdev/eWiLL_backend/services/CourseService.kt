package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Course
import com.wipdev.eWiLL_backend.database.tables.course.CourseUserRole
import com.wipdev.eWiLL_backend.database.tables.course.ECourseRole
import com.wipdev.eWiLL_backend.endpoints.payload.CourseEntry
import com.wipdev.eWiLL_backend.endpoints.payload.CourseUser
import com.wipdev.eWiLL_backend.repository.CourseRepository
import com.wipdev.eWiLL_backend.repository.CourseRoleRepository
import com.wipdev.eWiLL_backend.repository.UserRepository
import com.wipdev.eWiLL_backend.services.serviceInterfaces.ICourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class CourseService : ICourseService {

    @Autowired
    lateinit var repository: CourseRepository

    @Autowired
    lateinit var courseRoleRepository: CourseRoleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    override fun getAll(userId: Long): List<CourseEntry> {
        val list: MutableList<CourseEntry> = mutableListOf()
        repository.findAll()
            .forEach { list.add(CourseEntry(it, courseRoleRepository.existsByCourseIdAndUserId(it.id!!, userId))) }
        return list
    }

    override fun getById(id: Long): Course {
        return repository.findById(id).get()
    }

    override fun create(course: Course): Course {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        course.creationDate = formattedDateTime
        course.id = null
        val savedCourse = repository.save(course)
        val role = CourseUserRole()
        role.courseId = savedCourse.id!!
        role.userId = course.owner
        role.role = ECourseRole.OWNER
        courseRoleRepository.save(role)
        return savedCourse
    }

    override fun update(id: Long, course: Course): Course {
        val oldCourse = repository.findById(id).get()
        oldCourse.name = course.name
        oldCourse.description = course.description
        oldCourse.keyPassword = course.keyPassword
        oldCourse.owner = course.owner
        oldCourse.semester = course.semester
        oldCourse.location = course.location
        return repository.save(oldCourse)
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    override fun getCourseByUserId(id: Long): List<Course> {
        val list: MutableList<Course> = mutableListOf()
        repository.findAll().forEach { if (it.owner == id) list.add(it) }
        return list
    }

    override fun getStudentsByCourseId(id: Long): List<CourseUser> {
        val list: MutableList<CourseUser> = mutableListOf()
        courseRoleRepository.findAll().forEach {
            if (it.courseId == id)
                list.add(
                    CourseUser(
                        userRepository.findById(it.userId!!).get(),
                        courseRoleRepository.findByCourseIdAndUserId(id, it.userId!!)!!.role!!
                    )
                )
        }
        return list
    }

    override fun joinCourse(id: Long, keyPass: String, userId: Long): CourseUserRole {

        val course = repository.findById(id).get()
        if (course.keyPassword == keyPass) {
            if (!courseRoleRepository.existsByCourseIdAndUserId(id, userId)) {
                val courseUserRole = CourseUserRole()
                courseUserRole.courseId = id
                courseUserRole.userId = userId
                courseUserRole.role = ECourseRole.STUDENT
                courseRoleRepository.save(courseUserRole)
                return courseRoleRepository.findAll().first { it.courseId == id && it.userId == userId }
            } else {
                throw ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "User already in Course")
            }
        } else {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong KeyPass")
        }

    }

    override fun leaveCourse(id: Long, userId: Long): Course {
        val course = repository.findById(id).get()
        if (courseRoleRepository.existsByCourseIdAndUserId(id, userId)) {
            courseRoleRepository.deleteByCourseIdAndUserId(id, userId)
        }
        return course
    }

    override fun hasKeyPass(id: Long): Boolean {
        return repository.findById(id).get().keyPassword != null
    }

    override fun removeAllButOwner(id: Long): Course {
        courseRoleRepository.findAll().forEach {
            if (it.courseId == id && it.role != ECourseRole.OWNER) courseRoleRepository.delete(
                it
            )
        }
        return repository.findById(id).get()
    }

    override fun archiveCourse(id: Long): Course {
        val course = repository.findById(id).get()
        course.active = false
        return repository.save(course)
    }

    override fun changeUserRole(
        courseId: Long,
        userId: Long,
        role: ECourseRole,
        executorUserId: Long
    ): ResponseEntity<CourseUserRole> {
        val executorRole = getUserRoleInCourse(courseId, executorUserId)
        return if (executorRole == ECourseRole.OWNER || executorRole == ECourseRole.TUTOR) {
            val courseRole = courseRoleRepository.findByCourseIdAndUserId(courseId, userId)!!
            courseRole.role = role
            courseRoleRepository.save(courseRole)

            ResponseEntity(
                courseRoleRepository.findAll().first { it.courseId == courseId && it.userId == userId },
                HttpStatus.OK
            )
        } else {
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }

    override fun getUserRoleInCourse(courseId: Long, userId: Long): ECourseRole? {
        return try {
            courseRoleRepository.findAll().first { it.courseId == courseId && it.userId == userId }.role
        } catch (e: NoSuchElementException) {
            ECourseRole.NONE
        }
    }


}
