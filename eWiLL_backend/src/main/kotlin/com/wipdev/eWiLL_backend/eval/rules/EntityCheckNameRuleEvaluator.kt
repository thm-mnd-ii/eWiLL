package com.wipdev.eWiLL_backend.eval.rules

import com.wipdev.eWiLL_backend.endpoints.payload.requests.Entity
import com.wipdev.eWiLL_backend.eval.compile.DiagramData
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.utils.Dictionary
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils

class EntityCheckNameRuleEvaluator : IRuleEvaluator {



    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule,ruleConfig: RuleConfig): RuleEvalResult {
        var diagramData = diagramEvalPL.diagramData
        var solutionDiagramData = diagramEvalPL.solutionDiagramData

        diagramEvalPL.bestSolutionDiagram =  solutionDiagramData!![0] //TODO get best solution diagram not just the first one
        var stringBuilder = StringBuilder()
        var errors = 0
        for(node in diagramEvalPL.bestSolutionDiagram.nodes){
            val entityName = node.entity?.entityName
            val possibleNames = Dictionary.getPossibleNames(entityName!!)
            if(!StringFinderUtils.isPresent(entityName,possibleNames)){
                stringBuilder.append("Entity name $entityName is not correct. Possible names are: ${possibleNames.joinToString()}")//TODO Not expose every possible name
                errors++
            }
        }
        //TODO Create a new class to apply matchings from the solution to the diagram for further evaluation
        return RuleEvalResult(RuleEvalScore(errors,ScoreType.ERROR_COUNT),stringBuilder.toString(),rule.ruleType,rule.id)


    }

}
