package com.wipdev.eWiLL_backend.database.tables.course

import com.fasterxml.jackson.databind.ObjectMapper
import com.wipdev.eWiLL_backend.database.tables.Diagram
import javax.persistence.*

@Entity
@Table(name = "submission")
class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "userId", nullable = false)
    var userId: Long? = null

    @Column(name = "taskId", nullable = false)
    var taskId: Long? = null

    @Column(name = "date", nullable = false)
    var date: String? = null

    @Column(name = "diagram", nullable = false, length = 100000)
    var diagram: String? = null

    // Convert the diagram to a JSON string when setting the property
    fun setDiagram(diagram: Diagram) {
        val objectMapper = ObjectMapper()
        this.diagram = objectMapper.writeValueAsString(diagram)
    }

    // Convert the JSON string back to a Diagram object when getting the property
    fun getDiagram(): Diagram? {
        val objectMapper = ObjectMapper()
        return diagram?.let {
            objectMapper.readValue(it, Diagram::class.java)
        }
    }


    @Column(name = "attempt", nullable = false)
    var attempt: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Submission

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }


}
