package com.wipdev.eWiLL_backend.services;

import com.wipdev.eWiLL_backend.repository.CourseRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class CourseService {

    @Autowired
    lateinit var repository: CourseRepository


}
