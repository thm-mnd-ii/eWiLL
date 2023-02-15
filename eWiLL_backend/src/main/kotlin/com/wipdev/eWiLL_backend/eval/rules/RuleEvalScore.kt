package com.wipdev.eWiLL_backend.eval.rules

class RuleEvalScore {

    var score: Number = 100.0f

    var scoreType : ScoreType = ScoreType.PERCENTAGE

    constructor(score: Number, scoreType: ScoreType) {
        this.score = score
        this.scoreType = scoreType
    }
}
