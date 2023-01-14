package com.wipdev.eWiLL_backend.eval

import com.wipdev.eWiLL_backend.eval.rules.RuleEvalResult

class DiagramEvalResult(var ruleEvalResults: List<RuleEvalResult>) {

    var ruleScoreResults: Map<Int, Float> = HashMap()
    var score: Float? = null

    fun addToRuleEvalResults(ruleEvalResult: RuleEvalResult) {
        ruleEvalResults += ruleEvalResult
        ruleScoreResults += ruleEvalResult.ruleId to ruleEvalResult.score
    }

    //In percentage
    fun calculateScore(): DiagramEvalResult {
        var score = 0f
        ruleScoreResults.forEach { (_, value) ->
            score += value
        }
        this.score = score / ruleScoreResults.size.toFloat()
        return this
    }


}




