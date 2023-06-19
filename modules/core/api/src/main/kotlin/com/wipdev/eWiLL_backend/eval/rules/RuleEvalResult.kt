package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.RuleType

class RuleEvalResult(var score: RuleEvalScore, var messages: List<ResultMessage>, var ruleType: RuleType,var ruleId : Int){
    override fun toString(): String {
        return "RuleEvalResult(score=$score, messages=$messages, ruleType=$ruleType, ruleId=$ruleId)"
    }

}
