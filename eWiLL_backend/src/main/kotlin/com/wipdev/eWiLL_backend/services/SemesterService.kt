package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Semester
import com.wipdev.eWiLL_backend.repository.SemesterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SemesterService : ISemesterService{

    @Autowired
    lateinit var semesterRepository: SemesterRepository
    override fun getSemester(id: Long): Semester {
        return semesterRepository.findById(id).get()
    }

    override fun getSemesters(): List<Semester> {
        return semesterRepository.findAll()
    }

    override fun createSemester(semester: Semester): Semester {
        semesterRepository.save(semester)
        return semester
    }
}