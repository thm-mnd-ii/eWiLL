package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.RuleType
import com.wipdev.eWiLL_backend.eval.rules.attributeequivalence.AttributeEquivalenceEvaluator
import com.wipdev.eWiLL_backend.eval.rules.connectionequivalence.ConnectionEquivalenceEvaluator
import com.wipdev.eWiLL_backend.eval.rules.entityequivalence.EntityCheckNameRuleEvaluator

enum class Rule(var ruleEvaluator: IRuleEvaluator,var ruleType: RuleType) {

    ENTITY_EQUIVALENCE( EntityCheckNameRuleEvaluator(),RuleType.SEMANTIC),
    CONNECTION_EQUIVALENCE(ConnectionEquivalenceEvaluator(),RuleType.SEMANTIC),
    ATTRIBUTE_EQUIVALENCE(AttributeEquivalenceEvaluator(),RuleType.SEMANTIC);


    val id : Int
        get() = ordinal


}