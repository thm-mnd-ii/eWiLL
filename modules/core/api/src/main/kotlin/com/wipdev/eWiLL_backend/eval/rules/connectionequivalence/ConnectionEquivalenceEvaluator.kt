package com.wipdev.eWiLL_backend.eval.rules.connectionequivalence

import com.wipdev.eWiLL_backend.eval.HighlightLevel
import com.wipdev.eWiLL_backend.eval.ResultLevel
import com.wipdev.eWiLL_backend.eval.compile.DiagramConnection
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.*
import com.wipdev.eWiLL_backend.utils.translate.Dictionary
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils

class ConnectionEquivalenceEvaluator: IRuleEvaluator {
    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule): RuleEvalResult {
        var messages = mutableListOf<ResultMessage>()
        var errors = 0;
        for(diagramConnection in diagramEvalPL.bestSolutionDiagram.connections){
            if(diagramConnection.to.entity!!.left!! < diagramConnection.from.entity!!.left!!){
                messages.add(ResultMessage(ResultLevel.BASIC,"Entity FROM ${diagramConnection.to.entity!!.entityName} is connected TO the left to ${diagramConnection.from.entity!!.entityName}  ",diagramConnection.from.entity!!.id!!,"",HighlightLevel.INCORRECT,ResultMessageType.Connection,diagramConnection.to.entity!!.id!!))
                errors++
            }

            var connection = findConnection(diagramConnection,diagramEvalPL)
            if(connection != null){
                messages.add(ResultMessage(ResultLevel.BASIC,"Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} is present ",diagramConnection.from.entity!!.id!!,"",HighlightLevel.CORRECT,ResultMessageType.Connection,connection.to.entity!!.id!!))

                if(connection.type != diagramConnection.type){
                    messages.add(ResultMessage(ResultLevel.BASIC,"Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} has wrong type ",diagramConnection.from.entity!!.id!!,"",HighlightLevel.INCORRECT,ResultMessageType.Connection,connection.to.entity!!.id!!))
                    messages.add(ResultMessage(ResultLevel.INFO,"Correct type is ${connection.type}",diagramConnection.from.entity!!.id!!,"",
                        HighlightLevel.SUGGESTION,ResultMessageType.Connection,connection.to.entity!!.id!!))
                    errors++
                }else{
                    messages.add(ResultMessage(ResultLevel.BASIC,"Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} has correct type ",diagramConnection.from.entity!!.id!!,"",HighlightLevel.CORRECT,ResultMessageType.Connection,connection.to.entity!!.id!!))
                }
            }else{
                messages.add(ResultMessage(ResultLevel.BASIC,"Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} is not present ",diagramConnection.from.entity!!.id!!,"",HighlightLevel.INCORRECT,ResultMessageType.Connection,diagramConnection.to.entity!!.id!!))

                errors++
            }
        }
        return RuleEvalResult(RuleEvalScore((diagramEvalPL.bestSolutionDiagram.connections.size.toFloat()-errors)/diagramEvalPL.bestSolutionDiagram.connections.size.toFloat(), ScoreType.PERCENTAGE),messages,rule.ruleType,rule.id)
    }

    private fun findConnection(diagramConnection: DiagramConnection, diagramEvalPL: DiagramEvalPL):DiagramConnection? {
        var possibleNamesFrom = diagramConnection.from.entity?.entityName?.let { Dictionary.getPossibleNames(it) }
        var possibleNamesTo = diagramConnection.to.entity?.entityName?.let { Dictionary.getPossibleNames(it) }

        for(connection in diagramEvalPL.diagramData!!.connections){
            if(StringFinderUtils.isPresent(connection.from.entity?.entityName!!,possibleNamesFrom!!) && StringFinderUtils.isPresent(connection.to.entity?.entityName!!,possibleNamesTo!!)){
                return connection
            }
        }
        return null
    }
}