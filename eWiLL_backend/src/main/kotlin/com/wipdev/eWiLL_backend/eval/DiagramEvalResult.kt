package com.wipdev.eWiLL_backend.eval

import com.wipdev.eWiLL_backend.eval.rules.RuleEvalResult
import com.wipdev.eWiLL_backend.eval.rules.RuleEvalScore
import com.wipdev.eWiLL_backend.eval.rules.ScoreType

class DiagramEvalResult(var ruleEvalResults: List<RuleEvalResult>) {

    var score = 0f

    fun addToRuleEvalResults(ruleEvalResult: RuleEvalResult) {
        ruleEvalResults += ruleEvalResult
    }

    //In percentage
    fun calculateScore(): DiagramEvalResult {
        var score = 0f
        var percentageSum = 0f
        var percentageCount = 0
        ruleEvalResults.forEach {
            if (it.score.scoreType == ScoreType.PERCENTAGE) {
                percentageSum += it.score.score.toFloat()
                percentageCount++
            }
        }

        if (percentageCount != 0) {
            score += percentageSum / percentageCount
        }
        ruleEvalResults.forEach {
            if (it.score.scoreType == ScoreType.ERROR_COUNT) {
                score -= it.score.score.toInt()
            }
        }
        this.score = score

        return this;
    }
}




