package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.compile.DiagramData
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.utils.Dictionary

class EntityCheckNameRuleEvaluator : IRuleEvaluator {



    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule,ruleConfig: RuleConfig): RuleEvalResult {
        var diagramData = diagramEvalPL.diagramData
        var solutionDiagramData = diagramEvalPL.solutionDiagramData
        //TODO: implement
        var topScore = 0
        var topModel: DiagramData? = null
        for(solutionDiagram in solutionDiagramData!!) {
            var score = 0
            for(entity in solutionDiagram.nodes) {
                if(diagramData!!.nodes.any { Dictionary.getPossibleNames(it.entity!!.entityName!!).contains(entity.entity!!.entityName) }) {
                    score++
                }
            }
            if(score > topScore) {
                topScore = score
                topModel = solutionDiagram
            }

        }


        return RuleEvalResult(topScore.toFloat()/topModel!!.nodes.size.toFloat(), "Best SolutionModel with id:"+topModel.id+" has "+topScore +"/"+topModel!!.nodes.size+" right entities with id",rule.ruleType,rule.ordinal)
    }

}
