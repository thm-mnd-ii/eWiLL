package com.wipdev.eWiLL_backend.eval

import com.wipdev.eWiLL_backend.eval.compile.DiagramData
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.Rule

class SERMDiagramEvaluator : IDiagramEvaluator {

    override fun eval(diagramEvalEntry: DiagramEvalEntry): DiagramEvalResult {
        val diagramData = DiagramData.compile(diagram = diagramEvalEntry.diagram)
        val solutionData = diagramEvalEntry.solutionDiagram!!.map { DiagramData.compile(diagram = it) }
        val diagramEvalPL = DiagramEvalPL(diagramData = diagramData, solutionDiagramData = solutionData)

        val diagramEvalResult = DiagramEvalResult(emptyList())
        for (rule in Rule.values()) {
            val ruleResult = rule.ruleEvaluator.eval(diagramEvalPL, rule)
            diagramEvalResult.addToRuleEvalResults(ruleResult)
        }
        return diagramEvalResult.calculateScore()
    }


}