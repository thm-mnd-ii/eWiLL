package com.wipdev.eWiLL_backend.database.tables.course;

import javax.persistence.*;

@Entity
@Table(name = "semester")
class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "start_date", nullable = false)
    var startDate: String? = null

    @Column(name = "end_date", nullable = false)
    var endDate: String? = null
}
