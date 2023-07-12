package com.wipdev.eWiLL_backend.services.serviceInterfaces

import com.wipdev.eWiLL_backend.database.tables.course.Semester
import org.springframework.stereotype.Service

@Service
interface ISemesterService {

    fun getSemester(id: Long): Semester
    fun getSemesters(): List<Semester>
    fun createSemester(semester: Semester): Semester

}