package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.User
import com.wipdev.eWiLL_backend.database.tables.course.Course
import com.wipdev.eWiLL_backend.database.tables.course.CourseUserRole
import com.wipdev.eWiLL_backend.database.tables.course.ECourseRole
import com.wipdev.eWiLL_backend.endpoints.payload.CourseEntry
import com.wipdev.eWiLL_backend.services.CourseService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Description
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/course")
@Tag(name = "Course", description = "Course API")
class CourseController {


    @Autowired
    lateinit var service: CourseService

    @GetMapping("/all/{userId}")
    @ResponseBody
    fun getAll(@PathVariable userId: Long): List<CourseEntry>  = service.getAll(userId)

    @GetMapping("/{id}")
    @ResponseBody
    fun getById(@PathVariable id: Long): Course = service.getById(id)

    @PutMapping()
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun create(@RequestBody course: Course): Course = service.create(course)

    @PostMapping("/{id}")
    @ResponseBody
    fun update(@PathVariable id: Long, @RequestBody course: Course): Course = service.update(id, course)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
    @GetMapping("/user/{id}")
    @ResponseBody
    fun getCourseByUserId(@PathVariable id: Long): List<Course> = service.getCourseByUserId(id)

    @GetMapping("/{id}/students")
    @ResponseBody
    fun getStudentsByCourseId(@PathVariable id: Long): List<User> = service.getStudentsByCourseId(id)

    @PostMapping("/{id}/join")
    @ResponseBody
    fun joinCourse(@PathVariable id: Long,keyPass:String,userId : Long): CourseUserRole = service.joinCourse(id,keyPass,userId)

    @PostMapping("/{id}/leave")
    @ResponseBody
    fun leaveCourse(@PathVariable id: Long,userId : Long): Course = service.leaveCourse(id,userId)

    @GetMapping("/{id}/hasKeyPass")
    fun hasKeyPass( @PathVariable id: Long): Boolean = service.hasKeyPass(id)

    @PostMapping("/{id}/removeAllButOwner")
    @ResponseBody
    @Description("Removes all users from the course except the owner")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun removeAllButOwner(@PathVariable id: Long) = service.removeAllButOwner(id)

    @PostMapping("/{id}/archive")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun archiveCourse(@PathVariable id: Long) = service.archiveCourse(id)

    @PostMapping("/{id}/changeUserRole/{role}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun changeUserRole(@PathVariable id: Long, @PathVariable role: ECourseRole) = service.changeUserRole(id, role)

    @GetMapping("/{courseId}/user/{userId}/role")
    @ResponseBody
    fun getUserRoleInCourse(@PathVariable courseId: Long,@PathVariable userId: Long): ECourseRole? = service.getUserRoleInCourse(courseId, userId)
}