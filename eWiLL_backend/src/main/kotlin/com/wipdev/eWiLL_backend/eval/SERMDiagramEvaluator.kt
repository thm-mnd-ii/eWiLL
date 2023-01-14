package com.wipdev.eWiLL_backend.eval

import com.wipdev.eWiLL_backend.eval.compile.DiagramData
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL

class SERMDiagramEvaluator : IDiagramEvaluator {

    override fun eval(diagramEvalEntry: DiagramEvalEntry): DiagramEvalResult {
        var diagramData = DiagramData.compile(diagram = diagramEvalEntry.diagram)
        var solutionData = diagramEvalEntry.solutionDiagram!!.map { DiagramData.compile(diagram = it) }
        var diagramEvalPL = DiagramEvalPL(diagramData = diagramData, ruleset = diagramEvalEntry.ruleset, solutionDiagramData = solutionData)

        var diagramEvalResult = DiagramEvalResult( emptyList())
        for(rule in diagramEvalPL.ruleset!!.getRules()) {
            var ruleResult = rule.ruleEvaluator.eval(diagramEvalPL, rule,diagramEvalPL.ruleset!!.getRuleConfigs().first(){it.ruleId == rule.ordinal})
            diagramEvalResult.addToRuleEvalResults(ruleResult)
        }
        return diagramEvalResult.calculateScore()
    }


}