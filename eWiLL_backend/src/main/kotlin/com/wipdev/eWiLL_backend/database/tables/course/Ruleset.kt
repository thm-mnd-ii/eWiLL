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

    @Column(name = "rules", nullable = false, length = 100000)
    open var rules: String? = "[0,1,2]"

    @Column(name = "course_id", nullable = false, length = 100000)
    open var ruleConfigs : String? = null

    fun getRules():Array<Rule>{
        var ids:Array<Int> =  Json.mapper().readerForArrayOf(Int::class.java).readValue(rules)
        return Array(ids.size){ i-> Rule.values()[ids[i]]}
    }

    fun getRuleConfigs():Array<RuleConfig>{
        return Json.mapper().readerForArrayOf(RuleConfig::class.java).readValue(ruleConfigs)
    }
}

