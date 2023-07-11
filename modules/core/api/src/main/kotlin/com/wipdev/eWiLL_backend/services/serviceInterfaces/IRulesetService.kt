package com.wipdev.eWiLL_backend.services.serviceInterfaces

import com.wipdev.eWiLL_backend.endpoints.payload.requests.RulesetPl

interface IRulesetService {

    fun getRuleset(id:Long): RulesetPl
    fun getAllRulesets(): List<RulesetPl>
    fun createRuleset(ruleset: RulesetPl): RulesetPl
    fun updateRuleset(id:Long, ruleset: RulesetPl): RulesetPl
    fun deleteRuleset(id:Long)



}