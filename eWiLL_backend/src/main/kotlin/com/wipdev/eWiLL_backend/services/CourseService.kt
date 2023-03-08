package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.User
import com.wipdev.eWiLL_backend.database.tables.course.Course
import com.wipdev.eWiLL_backend.database.tables.course.CourseUserRole
import com.wipdev.eWiLL_backend.database.tables.course.ECourseRole
import com.wipdev.eWiLL_backend.endpoints.payload.CourseEntry
import com.wipdev.eWiLL_backend.repository.CourseRepository
import com.wipdev.eWiLL_backend.repository.CourseRoleRepository
import com.wipdev.eWiLL_backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CourseService: ICourseService {

    @Autowired
    lateinit var repository: CourseRepository

    @Autowired
    lateinit var courseRoleRepository: CourseRoleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    override fun getAll(userId:Long): List<CourseEntry> {
        val list : MutableList<CourseEntry> = mutableListOf()
        repository.findAll().forEach { list.add(CourseEntry(it,courseRoleRepository.existsByCourseIdAndUserId(it.id!!,userId))) }
        return list
    }

    override fun getById(id: Long): Course {
        return repository.findById(id).get()
    }

    override fun create(course: Course): Course {
        return repository.save(course)
    }

    override fun update(id: Long, course: Course): Course {
        val oldCourse = repository.findById(id).get()
        oldCourse.name = course.name
        oldCourse.description = course.description
        oldCourse.keyPassword = course.keyPassword
        oldCourse.owner = course.owner
        return repository.save(oldCourse)
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    override fun getCourseByUserId(id: Long): List<Course> {
        val list : MutableList<Course> = mutableListOf()
        repository.findAll().forEach { if(it.owner == id) list.add(it) }
        return list
    }

    override fun getStudentsByCourseId(id: Long): List<User> {
        val list : MutableList<User> = mutableListOf()
        courseRoleRepository.findAll().forEach { if(it.courseId == id && it.role == ECourseRole.STUDENT) list.add(userRepository.findById(it.userId!!).get()) }
        return list
    }

    override fun joinCourse(id: Long, keyPass: String, userId: Long): CourseUserRole {

        val course = repository.findById(id).get()
        if(course.keyPassword == keyPass){
            if(!courseRoleRepository.existsByCourseIdAndUserId(id,userId)){
                val courseUserRole = CourseUserRole()
                courseUserRole.courseId = id
                courseUserRole.userId = userId
                courseUserRole.role = ECourseRole.STUDENT
                courseRoleRepository.save(courseUserRole)
            }
        }
        return courseRoleRepository.findAll().first { it.courseId == id && it.userId == userId }

    }

    override fun leaveCourse(id: Long, userId: Long): Course {
        val course = repository.findById(id).get()
        if(courseRoleRepository.existsByCourseIdAndUserId(id,userId)){
            courseRoleRepository.deleteByCourseIdAndUserId(id,userId)
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

    override fun changeUserRole(id: Long, role: ECourseRole): CourseUserRole {
        val courseUserRole = courseRoleRepository.findById(id).get()
        courseUserRole.role = role
        return courseRoleRepository.save(courseUserRole)
    }

    override fun getUserRoleInCourse(courseId: Long, userId: Long): ECourseRole? {
        return courseRoleRepository.findAll().first { it.courseId == courseId && it.userId == userId }.role
    }


}
