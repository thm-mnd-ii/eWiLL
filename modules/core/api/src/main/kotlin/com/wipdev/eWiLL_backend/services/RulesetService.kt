package com.wipdev.eWiLL_backend.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.RulesetPl
import com.wipdev.eWiLL_backend.eval.rules.RuleConfig
import com.wipdev.eWiLL_backend.repository.RulesetRepository
import com.wipdev.eWiLL_backend.services.serviceInterfaces.IRulesetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RulesetService : IRulesetService {
    @Autowired
    lateinit var rulesetRepository: RulesetRepository

    override fun getRuleset(id: Long): RulesetPl {
        return convert(rulesetRepository.findById(id).get())
    }

    override fun getAllRulesets(): List<RulesetPl> {
        var rulesets = rulesetRepository.findAll()
        var rulesetPls = mutableListOf<RulesetPl>()
        for (ruleset in rulesets) {
            rulesetPls.add(convert(ruleset))
        }
        return rulesetPls
    }

    override fun createRuleset(ruleset: RulesetPl): RulesetPl {
        return convert(rulesetRepository.save(convert(ruleset)))
    }

    override fun updateRuleset(id: Long, ruleset: RulesetPl): RulesetPl {
        var rulesetDb = rulesetRepository.findById(id).get()
        rulesetDb.name = ruleset.name
        rulesetDb.ruleConfigs = getRuleConfigListString(ruleset.ruleConfigs!!)
        return convert(rulesetRepository.save(rulesetDb))
    }

    override fun deleteRuleset(id: Long) {
        rulesetRepository.deleteById(id)
    }

    fun convert(ruleset: Ruleset): RulesetPl {
        return RulesetPl(ruleset.id, ruleset.name,getRuleConfigList(ruleset.ruleConfigs!!))
    }

    fun convert(rulesetPl: RulesetPl): Ruleset {
        var ruleset : Ruleset = Ruleset()
        ruleset.id = rulesetPl.id
        ruleset.name = rulesetPl.name
        ruleset.ruleConfigs = getRuleConfigListString(rulesetPl.ruleConfigs!!)
        return ruleset
    }

    fun getRuleConfig(s : String) : RuleConfig {
        var jsonMapper = jacksonObjectMapper()
        return jsonMapper.readValue(s, RuleConfig::class.java)
    }

    fun getRuleConfigList(s : String) : List<RuleConfig> {
        var jsonMapper = jacksonObjectMapper()
        return jsonMapper.readValue(s, List::class.java) as List<RuleConfig>
    }

    fun getRuleConfigString(ruleConfig: RuleConfig) : String {
        var jsonMapper = jacksonObjectMapper()
        return jsonMapper.writeValueAsString(ruleConfig)
    }

    fun getRuleConfigListString(ruleConfigList: List<RuleConfig>) : String {
        var jsonMapper = jacksonObjectMapper()
        return jsonMapper.writeValueAsString(ruleConfigList)
    }
}
