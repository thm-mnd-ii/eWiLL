package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.RulesetPl
import com.wipdev.eWiLL_backend.repository.RulesetRepository
import org.springframework.beans.factory.annotation.Autowired

interface IRulesetService {

    fun getRuleset(id:Long): RulesetPl
    fun getAllRulesets(): List<RulesetPl>
    fun createRuleset(ruleset: RulesetPl): RulesetPl
    fun updateRuleset(id:Long, ruleset: RulesetPl): RulesetPl
    fun deleteRuleset(id:Long)



}