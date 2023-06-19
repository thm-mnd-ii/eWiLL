package com.wipdev.eWiLL_backend.eval

import com.wipdev.eWiLL_backend.database.tables.course.SubmissionResult
import com.wipdev.eWiLL_backend.eval.rules.RuleEvalResult

class DiagramEvalResult(private var ruleEvalResults: List<RuleEvalResult>) {

    private var score = 0f

    fun addToRuleEvalResults(ruleEvalResult: RuleEvalResult) {
        ruleEvalResults += ruleEvalResult
    }

    //In percentage
    fun calculateScore(): DiagramEvalResult {
        var score = 0f
        var percentageSum = 0f
        var percentageCount = 0
        ruleEvalResults.forEach {
            percentageSum += it.score.getScore()
            percentageCount++

        }



        if (percentageCount != 0) {
            score += percentageSum / percentageCount
        }
        this.score = score

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




