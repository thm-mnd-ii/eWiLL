package com.wipdev.eWiLL_backend.eval.rules.connectionequivalence

import com.wipdev.eWiLL_backend.eval.compile.DiagramConnection
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.*
import com.wipdev.eWiLL_backend.utils.translate.Dictionary
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils

class ConnectionEquivalenceEvaluator: IRuleEvaluator {
    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule, ruleConfig: RuleConfig): RuleEvalResult {
        var stringbuilder = StringBuilder()
        var errors = 0;
        for(diagramConnection in diagramEvalPL.bestSolutionDiagram.connections){
            if(diagramConnection.to.entity!!.left!! < diagramConnection.from.entity!!.left!!){
                stringbuilder.append("Entity ${diagramConnection.to.entity!!.entityName} is left to ${diagramConnection.from.entity!!.entityName} ")
                errors++
            }

            var connection = findConnection(diagramConnection,diagramEvalPL)
            if(connection != null){
                stringbuilder.append("Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} is present ")
                if(connection.type != diagramConnection.type){
                    stringbuilder.append("Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} has wrong type ")
                    errors++
                }else{
                    stringbuilder.append("Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} has correct type ")
                }
            }else{
                stringbuilder.append("Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} is not present ")
                errors++
            }
        }
        return RuleEvalResult(RuleEvalScore(errors, ScoreType.ERROR_COUNT),stringbuilder.toString(),rule.ruleType,rule.id)
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