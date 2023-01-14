package com.wipdev.eWiLL_backend.eval

import com.wipdev.eWiLL_backend.eval.rules.RuleEvalResult

class DiagramEvalResult(var score : Long?,var ruleEvalResults: List<RuleEvalResult>) {


    fun addToRuleEvalResults(ruleEvalResult: RuleEvalResult): DiagramEvalResult {
        return DiagramEvalResult(score,ruleEvalResults.plus(ruleEvalResult))
    }


}




