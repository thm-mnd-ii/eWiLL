package com.wipdev.eWiLL_backend.database.tables.course

import com.wipdev.eWiLL_backend.eval.rules.Rule
import com.wipdev.eWiLL_backend.eval.rules.RuleConfig
import io.swagger.v3.core.util.Json
import javax.persistence.*

@Entity
@Table(name = "ruleset")
data class Ruleset(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = 0,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "course_id", nullable = false, length = 100000)
    var ruleConfigs: String? = null,
) {
    fun getRules(): Array<Rule> {
        val arr: Array<RuleConfig> = Json.mapper().readerForArrayOf(RuleConfig::class.java).readValue(ruleConfigs)
        val rules = mutableListOf<Rule>()
        for (ruleConfig in arr) {
            rules.add(Rule.values()[ruleConfig.ruleId!!])
        }
        return rules.toTypedArray()
    }

    fun getRuleConfigs(): Array<RuleConfig> {
        return Json.mapper().readerForArrayOf(RuleConfig::class.java).readValue(ruleConfigs)
    }

    override fun toString(): String {
        return "Ruleset(id=$id, name=$name, ruleConfigs=$ruleConfigs)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Ruleset
        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
