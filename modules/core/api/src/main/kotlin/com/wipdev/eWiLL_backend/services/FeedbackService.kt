package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Feedback
import com.wipdev.eWiLL_backend.endpoints.payload.requests.FeedbackPl
import com.wipdev.eWiLL_backend.repository.FeedbackRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FeedbackService {

    @Autowired
    lateinit var feedbackRepository: FeedbackRepository
    fun findAll(): List<Feedback> {
        return feedbackRepository.findAll()
    }

    fun save(feedback: Feedback) {
        feedbackRepository.save(feedback)
    }

    fun delete(id: Long) {
        feedbackRepository.deleteById(id)
    }

    fun findById(id: Long): Feedback {
        return feedbackRepository.findById(id).get()
    }

    fun update(id: Long, feedbackPl: FeedbackPl) {
        val feedback = findById(id)
        feedback.text = feedbackPl.text
        feedback.firstName = feedbackPl.firstName
        feedback.lastName = feedbackPl.lastName
        feedback.status = feedbackPl.status
        feedbackRepository.save(feedback)
    }


}
