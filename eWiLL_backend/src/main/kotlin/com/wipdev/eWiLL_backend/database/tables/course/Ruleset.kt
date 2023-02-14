package com.wipdev.eWiLL_backend.database.tables.course

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

    @Column(name = "rules", nullable = false)
    open var rules: String? = null

    @Column(name = "course_id", nullable = false, length = 100000)
    open var ruleConfigs : String? = null

    fun setRules(rules:List<Rule>){
        this.rules = Json.mapper().writeValueAsString(rules)
    }
    fun getRules():Array<Rule>{
        var ids:Array<Int> =  Json.mapper().readerForArrayOf(Int::class.java).readValue(rules)
        return Array(ids.size){ i-> Rule.values()[ids[i]]}
    }

    fun setRuleConfigs(ruleConfigs:List<RuleConfig>){
        this.ruleConfigs = Json.mapper().writeValueAsString(ruleConfigs)
    }

    fun getRuleConfigs():Array<RuleConfig>{
        return Json.mapper().readerForArrayOf(RuleConfig::class.java).readValue(ruleConfigs)
    }
}

