package com.wipdev.eWiLL_backend.database.tables.course

import com.wipdev.eWiLL_backend.MediaType
import com.wipdev.eWiLL_backend.database.tables.EFeedbackStatus
import javax.persistence.*

@Entity
@Table(name = "feedback")
class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null


    @Column(name="text", nullable = true, length = 100000)
    var text: String? = null

    @Column(name="firstName", nullable = true)
    var firstName: String? = null

    @Column(name="lastName", nullable = true)
    var lastName: String? = null

    @Column(name="times", nullable = true)
    var timeStamp:String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    var status: EFeedbackStatus? = EFeedbackStatus.NotReviewed

}