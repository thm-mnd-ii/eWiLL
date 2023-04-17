package com.wipdev.eWiLL_backend.repository

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RulesetRepository : CrudRepository<Ruleset, Long>