package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.course.Feedback
import com.wipdev.eWiLL_backend.repository.FeedbackRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feedback")
class FeedbackController {

    @Autowired
    lateinit var feedbackRepository: FeedbackRepository

    @PostMapping("/create")
    fun createFeedback(@RequestBody text: String) {
        val feedback = Feedback()
        feedback.text = text
        feedbackRepository.save(feedback)
    }
}