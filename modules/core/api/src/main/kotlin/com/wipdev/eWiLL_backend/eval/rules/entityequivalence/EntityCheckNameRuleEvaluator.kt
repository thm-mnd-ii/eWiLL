package com.wipdev.eWiLL_backend.eval.rules.entityequivalence

import com.wipdev.eWiLL_backend.eval.HighlightLevel
import com.wipdev.eWiLL_backend.eval.ResultLevel
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.*
import com.wipdev.eWiLL_backend.utils.translate.Dictionary
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils

class EntityCheckNameRuleEvaluator : IRuleEvaluator {



    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule): RuleEvalResult {
        var diagramData = diagramEvalPL.diagramData
        var solutionDiagramData = diagramEvalPL.solutionDiagramData

        diagramEvalPL.bestSolutionDiagram =  solutionDiagramData!![0] //TODO get best solution diagram not just the first one
        var messages = mutableListOf<ResultMessage>()
        var errors = 0
        for(node in diagramEvalPL.bestSolutionDiagram.nodes){
            val entityName = node.entity?.entityName
            val possibleNames = Dictionary.getPossibleNames(entityName!!)
            if(!StringFinderUtils.isPresent(entityName,possibleNames)){
                messages.add(ResultMessage(ResultLevel.BASIC,"Entity name $entityName is not correct.",node.entity!!.id!!,"",
                    HighlightLevel.INCORRECT,ResultMessageType.Entity))
                messages.add(ResultMessage(ResultLevel.INFO,"Possible names are: ${possibleNames.joinToString()}",node.entity!!.id!!,entityName,HighlightLevel.SUGGESTION,ResultMessageType.Entity))
                errors++
            }else{
                val otherNode = diagramData!!.getNodeByName(entityName)
                if(otherNode == null){
                    messages.add(ResultMessage(ResultLevel.BASIC,"Entity name $entityName is not present in the solution.",node.entity!!.id!!,"",
                        HighlightLevel.INCORRECT,   ResultMessageType.Entity))
                    messages.add(ResultMessage(ResultLevel.INFO,"Possible names are: ${possibleNames.joinToString()}",node.entity!!.id!!,entityName,HighlightLevel.SUGGESTION,  ResultMessageType.Entity))
                    errors++
                    continue
                }
                node.otherModelNode = otherNode
                node.otherModelNode!!.otherModelNode = node
                messages.add(ResultMessage(ResultLevel.BASIC,"Entity name $entityName is correct.",node.entity!!.id!!,"",HighlightLevel.CORRECT,ResultMessageType.Entity))
                messages.add(ResultMessage(ResultLevel.DEBUG,"It maps to ${node.otherModelNode!!.entity!!.entityName}",node.entity!!.id!!,"",HighlightLevel.CORRECT,ResultMessageType.Entity))
            }
        }

        return RuleEvalResult(RuleEvalScore((diagramEvalPL.bestSolutionDiagram.nodes.size.toFloat()-errors)/diagramEvalPL.bestSolutionDiagram.nodes.size.toFloat(), ScoreType.PERCENTAGE),messages,rule.ruleType,rule.id)


    }

}
