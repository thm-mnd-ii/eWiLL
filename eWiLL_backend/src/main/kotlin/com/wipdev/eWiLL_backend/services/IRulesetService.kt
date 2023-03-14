package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.repository.RulesetRepository
import org.springframework.beans.factory.annotation.Autowired

interface IRulesetService {

    fun getRuleset(id:Long): Ruleset
    fun getAllRulesets(): List<Ruleset>
    fun createRuleset(ruleset: Ruleset): Ruleset
    fun updateRuleset(id:Long, ruleset: Ruleset): Ruleset
    fun deleteRuleset(id:Long)



}