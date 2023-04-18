package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.EFeedbackStatus
import com.wipdev.eWiLL_backend.database.tables.course.Feedback
import com.wipdev.eWiLL_backend.endpoints.payload.requests.FeedbackPl
import com.wipdev.eWiLL_backend.services.FeedbackService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/feedback")
class FeedbackController {

    @Autowired
    lateinit var feedbackService : FeedbackService

    @PostMapping("/create")
    fun createFeedback(@RequestBody feedbackPl: FeedbackPl) {
        val feedback = Feedback()
        feedback.text = feedbackPl.text
        feedback.firstName = feedbackPl.firstName
        feedback.lastName = feedbackPl.lastName
        feedback.status = EFeedbackStatus.NotReviewed
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        feedback.timeStamp = formattedDateTime
        feedbackService.save(feedback)
    }



    @PostMapping("/get")
    @ResponseBody
    fun getFeedback(): List<Feedback> {
        return feedbackService.findAll()
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteFeedback(@PathVariable id: Long) {
        feedbackService.delete(id)
    }

    @GetMapping("/statuses")
    fun getStatuses(): Array<EFeedbackStatus> {
        return EFeedbackStatus.values()
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun updateFeedback(@PathVariable id: Long, @RequestBody feedbackPl: FeedbackPl) {
        val feedback = feedbackService.findById(id)
        feedback.text = feedbackPl.text
        feedback.firstName = feedbackPl.firstName
        feedback.lastName = feedbackPl.lastName
        feedback.status = feedbackPl.status
        feedbackService.save(feedback)
    }

}