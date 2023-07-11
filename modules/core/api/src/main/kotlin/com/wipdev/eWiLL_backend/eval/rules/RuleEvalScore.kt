package com.wipdev.eWiLL_backend.eval.rules

class RuleEvalScore {

    var score: Float = 100.0f

    var scoreType : ScoreType = ScoreType.ERROR_COUNT

    constructor(score: Float, scoreType: ScoreType) {
        this.score = score
        this.scoreType = scoreType
    }

    override fun toString(): String {
        return "RuleEvalScore(score=$score, scoreType=$scoreType)"
    }

}
