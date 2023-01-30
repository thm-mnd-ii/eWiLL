package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.RuleType

enum class Rule(var ruleEvaluator: IRuleEvaluator,var ruleType: RuleType) {

    ENTITY_CHECK_NAME( EntityCheckNameRuleEvaluator(),RuleType.SEMANTIC),
    ENTITY_LEFT_TO_RIGHT_ORDER(EntityLeftToRightOrderRule(),RuleType.SEMANTIC);


    val id : Int
        get() = ordinal


}