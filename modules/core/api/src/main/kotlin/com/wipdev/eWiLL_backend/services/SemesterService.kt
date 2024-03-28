package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Semester
import com.wipdev.eWiLL_backend.repository.SemesterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SemesterService {

    @Autowired
    lateinit var semesterRepository: SemesterRepository
    fun getSemester(id: Long): Semester {
        return semesterRepository.findById(id).get()
    }

    fun getSemesters(): List<Semester> {
        return semesterRepository.findAll()
    }

    fun createSemester(semester: Semester): Semester {
        semesterRepository.save(semester)
        return semester
    }
}