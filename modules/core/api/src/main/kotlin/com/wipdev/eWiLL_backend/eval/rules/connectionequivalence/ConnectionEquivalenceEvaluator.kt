package com.wipdev.eWiLL_backend.eval.rules.connectionequivalence

import com.wipdev.eWiLL_backend.eval.FeedbackLevel
import com.wipdev.eWiLL_backend.eval.StatusLevel
import com.wipdev.eWiLL_backend.eval.compile.ConnectionType
import com.wipdev.eWiLL_backend.eval.compile.DiagramConnection
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.*
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils
import com.wipdev.eWiLL_backend.utils.translate.Dictionary

class ConnectionEquivalenceEvaluator : IRuleEvaluator {

    val stringSimmilarity = 0.8
    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule): RuleEvalResult {
        val messages = mutableListOf<ResultMessage>()
        var errors = 0
        for (diagramConnection in diagramEvalPL.bestSolutionDiagram.connections) {
            if (diagramConnection.from.otherModelNode == null || diagramConnection.to.otherModelNode == null) {
                messages.add(
                    ResultMessage(
                        FeedbackLevel.DEBUG,
                        "Connection can be skipped, because one of the entities is missing.",
                        diagramConnection.from.entity!!.id!!,
                        "",
                        StatusLevel.INCORRECT,
                        ResultMessageType.Connection,
                        diagramConnection.to.entity!!.id!!
                    )
                )
                errors++
                continue
            }
            if (diagramConnection.to.entity!!.left!! < diagramConnection.from.entity!!.left!!) {
                messages.add(
                    ResultMessage(
                        FeedbackLevel.BASIC,
                        "Entity FROM ${diagramConnection.to.entity!!.entityName} is connected TO the left to ${diagramConnection.from.entity!!.entityName}  ",
                        diagramConnection.from.entity!!.id!!,
                        "",
                        StatusLevel.INCORRECT,
                        ResultMessageType.Connection,
                        diagramConnection.to.entity!!.id!!
                    )
                )
                errors++
            }

            val connection = findConnection(diagramConnection, diagramEvalPL)
            if (connection != null) {
                messages.add(
                    ResultMessage(
                        FeedbackLevel.DEBUG,
                        "Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} is present ",
                        diagramConnection.from.entity!!.id!!,
                        "",
                        StatusLevel.CORRECT,
                        ResultMessageType.Connection,
                        connection.to.entity!!.id!!
                    )
                )

                if (connection.type != diagramConnection.type) {
                    messages.add(
                        ResultMessage(
                            FeedbackLevel.BASIC,
                            "Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} has wrong type ",
                            diagramConnection.from.entity!!.id!!,
                            "",
                            StatusLevel.INCORRECT,
                            ResultMessageType.Connection,
                            connection.to.entity!!.id!!
                        )
                    )
                    messages.add(
                        ResultMessage(
                            FeedbackLevel.INFO,
                            "Correct type is ${ConnectionType.fromInt(connection.type)}",
                            diagramConnection.from.entity!!.id!!,
                            "",
                            StatusLevel.SUGGESTION,
                            ResultMessageType.Connection,
                            connection.to.entity!!.id!!
                        )
                    )
                    errors++
                } else {
                    messages.add(
                        ResultMessage(
                            FeedbackLevel.DEBUG,
                            "Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} has correct type ",
                            diagramConnection.from.entity!!.id!!,
                            "",
                            StatusLevel.CORRECT,
                            ResultMessageType.Connection,
                            connection.to.entity!!.id!!
                        )
                    )
                }
            } else {
                messages.add(
                    ResultMessage(
                        FeedbackLevel.INFO,
                        "Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} is not present ",
                        diagramConnection.from.entity!!.id!!,
                        "",
                        StatusLevel.INCORRECT,
                        ResultMessageType.Connection,
                        diagramConnection.to.entity!!.id!!
                    )
                )

                errors++
            }
        }
        //Check for connections present in the model but not in the solutuion
        for (diagramConnection in diagramEvalPL.diagramData!!.connections) {
            if (diagramConnection.from.otherModelNode == null || diagramConnection.to.otherModelNode == null) {
                continue
            }
            val connection = findConnection(diagramConnection, diagramEvalPL)
            if (connection == null) {
                messages.add(
                    ResultMessage(
                        FeedbackLevel.INFO,
                        "Connection ${diagramConnection.from.entity!!.entityName} to ${diagramConnection.to.entity!!.entityName} is present in the model but not in the solution ",
                        diagramConnection.from.entity!!.id!!,
                        "",
                        StatusLevel.INCORRECT,
                        ResultMessageType.Connection,
                        diagramConnection.to.entity!!.id!!
                    )
                )
                errors++
            }
        }

        return RuleEvalResult(
            RuleEvalScore(
                (diagramEvalPL.bestSolutionDiagram.connections.size.toFloat() - errors) / diagramEvalPL.bestSolutionDiagram.connections.size.toFloat(),
                ScoreType.PERCENTAGE
            ), messages, rule.ruleType, rule.id
        )
    }

    private fun findConnection(diagramConnection: DiagramConnection, diagramEvalPL: DiagramEvalPL): DiagramConnection? {
        val possibleNamesFrom = diagramConnection.from.entity?.entityName?.let { Dictionary.getPossibleNames(it) }
        val possibleNamesTo = diagramConnection.to.entity?.entityName?.let { Dictionary.getPossibleNames(it) }

        for (connection in diagramEvalPL.diagramData!!.connections) {
            if (StringFinderUtils.isPresent(
                    connection.from.entity?.entityName!!, possibleNamesFrom!!, stringSimmilarity, true
                ) && StringFinderUtils.isPresent(
                    connection.to.entity?.entityName!!,
                    possibleNamesTo!!,
                    stringSimmilarity,
                    true
                )
            ) {
                return connection
            }
        }
        return null
    }
}