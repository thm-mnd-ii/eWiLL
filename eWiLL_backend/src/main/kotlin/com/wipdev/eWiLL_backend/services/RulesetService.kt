package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.repository.RulesetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RulesetService : IRulesetService{
    @Autowired
    lateinit var rulesetRepository: RulesetRepository

    override fun getRuleset(id: Long): Ruleset {
        return rulesetRepository.findById(id).get()
    }

    override fun getAllRulesets(): List<Ruleset> {
        return rulesetRepository.findAll().toCollection(ArrayList<Ruleset>())
    }

    override fun createRuleset(ruleset: Ruleset): Ruleset {
        return rulesetRepository.save(ruleset)
    }

    override fun updateRuleset(id: Long, ruleset: Ruleset): Ruleset {
        var old = rulesetRepository.findById(id).get()
        old.name = ruleset.name
        old.rules = ruleset.rules
        old.ruleConfigs = ruleset.ruleConfigs
        return rulesetRepository.save(old)
    }

    override fun deleteRuleset(id: Long) {
        rulesetRepository.deleteById(id)
    }

}
