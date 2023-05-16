package com.wipdev.eWiLL_backend.database.tables

import com.wipdev.eWiLL_backend.MediaType
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "Task")
data class Task(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "description", nullable = false, length = 100000)
    var description: String? = null,

    @Column(name = "dueDate", nullable = false)
    var dueDate: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "mediatype", nullable = false)
    var mediaType: MediaType? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "liability")
    var eLiability: ELiability? = ELiability.NONE,

    @Column(name = "courseId", nullable = false)
    var courseId: Long? = null,

    @Column(name = "solutionModelId", nullable = true)
    var solutionModelId: Long? = null,

    @Column(name = "rulesetId", nullable = false)
    var rulesetId: Long? = null,

    @Column(name="maxSubmissions", nullable = false)
    var maxSubmissions: Int? = 999
) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Task

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , description = $description , dueDate = $dueDate , mediaType = $mediaType , liability = $eLiability , courseId = $courseId , solutionModelId = $solutionModelId , rulesetId = $rulesetId )"
    }
}
