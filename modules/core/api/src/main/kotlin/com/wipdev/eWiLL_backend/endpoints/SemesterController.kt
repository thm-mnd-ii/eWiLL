package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.course.Semester
import com.wipdev.eWiLL_backend.services.SemesterService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/semester")
@Tag(name = "Semester", description = "Semester API used for course management")
class SemesterController {

    @Autowired
    lateinit var semesterService: SemesterService

    @GetMapping("/{id}")
    @ResponseBody
    fun getSemester(@PathVariable id: Long) = semesterService.getSemester(id)

    @GetMapping
    @ResponseBody
    fun getSemesters() = semesterService.getSemesters()

    @PostMapping
    @ResponseBody
    fun createSemester(@RequestBody semester: Semester) = semesterService.createSemester(semester)
}