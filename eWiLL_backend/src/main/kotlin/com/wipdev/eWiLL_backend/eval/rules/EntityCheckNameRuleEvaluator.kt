package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL

class EntityCheckNameRuleEvaluator : IRuleEvaluator {



    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule,ruleConfig: RuleConfig): RuleEvalResult {
        var diagramData = diagramEvalPL.diagramData
        var solutionDiagramData = diagramEvalPL.solutionDiagramData


    }

}
