package com.wipdev.eWiLL_backend.eval

import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import com.wipdev.eWiLL_backend.eval.rules.RuleEvalResult
import com.wipdev.eWiLL_backend.eval.rules.ScoreType

class DiagramEvalResult(private var ruleEvalResults: List<RuleEvalResult>) {

    private var score = 0f

    fun addToRuleEvalResults(ruleEvalResult: RuleEvalResult) {
        ruleEvalResults += ruleEvalResult
    }

    //In percentage
    fun calculateScore(): DiagramEvalResult {

        var percentageSum = 0f
        ruleEvalResults.forEach {
            if (it.score.scoreType == ScoreType.PERCENTAGE) {
                percentageSum += it.score.score * 100
            } else if (it.score.scoreType == ScoreType.ERROR_COUNT) {
                percentageSum += (100 - it.score.score)
            }
        }
        println(ruleEvalResults.joinToString { it.toString() } + " " + percentageSum +"/"+ ruleEvalResults.size + " = " + percentageSum / ruleEvalResults.size)
        this.score = percentageSum / ruleEvalResults.size
        return this
    }

    fun getResult(submissionId: Long): SubmissionResult {
        val submissionResult = SubmissionResult()

        submissionResult.correct = score >= 100
        submissionResult.score = score
        for (ruleEvalResult in ruleEvalResults) {
            for (resultMessage in ruleEvalResult.messages) {
                submissionResult.addResultMessage(resultMessage)
            }
        }
        submissionResult.submissionId = submissionId
        return submissionResult
    }

    override fun toString(): String {
        return "DiagramEvalResult(score=$score, ruleEvalResults=$ruleEvalResults)"
    }
}




