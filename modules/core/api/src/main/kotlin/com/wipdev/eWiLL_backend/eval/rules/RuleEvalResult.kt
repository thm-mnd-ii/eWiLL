package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.RuleType

class RuleEvalResult(var score: RuleEvalScore, var message: String?, var ruleType: RuleType,var ruleId : Int){
    override fun toString(): String {
        return "RuleEvalResult(score=$score, message=$message, ruleType=$ruleType, ruleId=$ruleId)"
    }

}
