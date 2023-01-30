package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL

class EntityLeftToRightOrderRule:IRuleEvaluator {
    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule, ruleConfig: RuleConfig): RuleEvalResult {
        var stringbuilder = StringBuilder()
        var errors = 0;
        for(diagramConnection in diagramEvalPL.diagramData!!.connections){
            if(diagramConnection.to.entity!!.left!! < diagramConnection.from.entity!!.left!!){
                stringbuilder.append("Entity ${diagramConnection.to.entity!!.entityName} is left to ${diagramConnection.from.entity!!.entityName} ")
                errors++
            }
        }
        return RuleEvalResult(RuleEvalScore(errors,ScoreType.ERROR_COUNT),stringbuilder.toString(),rule.ruleType,rule.id)
    }
}