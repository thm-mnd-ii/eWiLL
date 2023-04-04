package com.wipdev.eWiLL_backend.database.tables.course

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wipdev.eWiLL_backend.eval.rules.Rule
import com.wipdev.eWiLL_backend.eval.rules.RuleConfig
import io.swagger.v3.core.util.Json
import javax.persistence.*

@Entity
@Table(name = "ruleset")
class Ruleset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = 0

    @Column(name = "name", nullable = false)
    open var name: String? = null



    @Column(name = "course_id", nullable = false, length = 100000)
    open var ruleConfigs : String? = null

    fun getRules():Array<Rule>{
        var arr:Array<RuleConfig> = Json.mapper().readerForArrayOf(RuleConfig::class.java).readValue(ruleConfigs)
        var rules = mutableListOf<Rule>()
        for (ruleConfig in arr){
            rules.add(Rule.values()[ruleConfig.ruleId!!])
        }
        return rules.toTypedArray()
    }

    fun getRuleConfigs():Array<RuleConfig>{
        return Json.mapper().readerForArrayOf(RuleConfig::class.java).readValue(ruleConfigs)
    }
}

