package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import org.springframework.data.repository.CrudRepository

interface RulesetRepository : CrudRepository<Ruleset, Long> {

}