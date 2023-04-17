package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.course.Semester
import com.wipdev.eWiLL_backend.services.SemesterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/semester")
class SemesterController {

    @Autowired
    lateinit var semesterService: SemesterService

    @GetMapping("/{id}")
    @ResponseBody
    fun getSemester( @PathVariable id: Long) = semesterService.getSemester(id)

    @GetMapping
    @ResponseBody
    fun getSemesters() = semesterService.getSemesters()

    @PostMapping
    @ResponseBody
    fun createSemester(@RequestBody semester: Semester) = semesterService.createSemester(semester)
}