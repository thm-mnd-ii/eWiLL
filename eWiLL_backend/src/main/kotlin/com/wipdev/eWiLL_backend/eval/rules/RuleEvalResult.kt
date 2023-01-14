package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.DiagramEvalResult
import com.wipdev.eWiLL_backend.eval.RuleType

class RuleEvalResult(var score : Long?, var message : String?, var ruleType : RuleType) {

    fun addToDiagramEvalResult(diagramEvalResult: DiagramEvalResult): DiagramEvalResult {
        return diagramEvalResult.addToRuleEvalResults(this)
    }
}
