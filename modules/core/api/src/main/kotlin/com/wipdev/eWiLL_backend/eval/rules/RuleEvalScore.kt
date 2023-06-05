package com.wipdev.eWiLL_backend.eval.rules

class RuleEvalScore {

    var score: Number = 100.0f

    var scoreType : ScoreType = ScoreType.PERCENTAGE

    constructor(score: Number, scoreType: ScoreType) {
        this.score = score
        this.scoreType = scoreType
    }

    override fun toString(): String {
        return "RuleEvalScore(score=$score, scoreType=$scoreType)"
    }

    fun getScore():Float{
        return when(scoreType){
            ScoreType.PERCENTAGE -> score.toFloat()
            ScoreType.ERROR_COUNT -> 100 - score.toFloat()
            else -> 0f
        }
    }
}
