package com.wipdev.eWiLL_backend.eval.rules.entityequivalence

import com.wipdev.eWiLL_backend.endpoints.payload.requests.Entity
import com.wipdev.eWiLL_backend.eval.FeedbackLevel
import com.wipdev.eWiLL_backend.eval.StatusLevel
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.*
import com.wipdev.eWiLL_backend.utils.translate.Dictionary

class EntityCheckNameRuleEvaluator : IRuleEvaluator {


    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule): RuleEvalResult {
        val diagramData = diagramEvalPL.diagramData
        val solutionDiagramData = diagramEvalPL.solutionDiagramData

        diagramEvalPL.bestSolutionDiagram =
            solutionDiagramData!![0] //TODO get best solution diagram not just the first one
        val messages = mutableListOf<ResultMessage>()
        val foundEntities = mutableListOf<Entity>()
        val wrongTypedEntity = mutableListOf<Entity>()
        for (node in diagramEvalPL.bestSolutionDiagram.nodes) {
            val entityName = node.entity?.entityName
            val possibleNames = Dictionary.getPossibleNames(entityName!!)
            val otherNode = diagramData!!.getNodeByPossibleNames(possibleNames, 0.8, true)
            if (otherNode == null) {
                messages.add(
                    ResultMessage(
                        FeedbackLevel.INFO,
                        "Entity $entityName is not present in the solution.",
                        node.entity!!.id!!,
                        node.entity!!.entityName!!,
                        "",
                        StatusLevel.INCORRECT,
                        ResultMessageType.Entity
                    )
                )
            } else {
                node.otherModelNode = otherNode
                node.otherModelNode!!.otherModelNode = node
                foundEntities.add(node.entity!!)
                //Entity type check
                if (node.entity!!.type != otherNode.entity!!.type) {
                    messages.add(
                        ResultMessage(
                            FeedbackLevel.DEBUG,
                            "Entity ${node.entity!!.entityName} is type ${node.entity!!.type} but should be ${otherNode.entity!!.type} from ${otherNode.entity!!.entityName}.",
                            node.entity!!.id!!,
                            node.entity!!.entityName!!,
                            "",
                            StatusLevel.INCORRECT,
                            ResultMessageType.Entity
                        )
                    )
                    wrongTypedEntity.add(node.entity!!)
                }
            }

        }


        val missingEntities = diagramEvalPL.bestSolutionDiagram.nodes.size - foundEntities.size
        if (missingEntities > 0) {
            messages.add(
                ResultMessage(
                    FeedbackLevel.BASIC, "$missingEntities entities are missing in the solution.", -1, "", "",
                    StatusLevel.INCORRECT, ResultMessageType.Entity
                )
            )
        }
        val wrongTypedEntityCount = wrongTypedEntity.size
        if (wrongTypedEntityCount > 0) {
            messages.add(
                ResultMessage(
                    FeedbackLevel.BASIC, "$wrongTypedEntityCount entities are of wrong type.", -1, "", "",
                    StatusLevel.INCORRECT, ResultMessageType.Entity
                )
            )
        }

        val unnecessaryEntities = mutableListOf<Entity>()
        for (node in diagramData!!.nodes) {
            if (node.otherModelNode == null) {
                unnecessaryEntities.add(node.entity!!)
                messages.add(
                    ResultMessage(
                        FeedbackLevel.DEBUG,
                        "Entity ${node.entity!!.entityName} is not required.",
                        node.entity!!.id!!,
                        node.entity!!.entityName!!,
                        "",
                        StatusLevel.INCORRECT,
                        ResultMessageType.Entity
                    )
                )
            }
        }
        val unnecessaryEntitiesCount = unnecessaryEntities.size
        if (unnecessaryEntitiesCount > 0) {
            messages.add(
                ResultMessage(
                    FeedbackLevel.BASIC, "$unnecessaryEntitiesCount entities are present but not required.", -1, "", "",
                    StatusLevel.INCORRECT, ResultMessageType.Entity
                )
            )
            for (entity in unnecessaryEntities) {
                messages.add(
                    ResultMessage(
                        FeedbackLevel.INFO,
                        "Entity ${entity.entityName} is not required.",
                        entity.id!!,
                        entity.entityName!!,
                        "",
                        StatusLevel.INCORRECT,
                        ResultMessageType.Entity
                    )
                )
            }
        }
        val totalEntities = diagramEvalPL.bestSolutionDiagram.nodes.size
        val score =
            (totalEntities - missingEntities - wrongTypedEntityCount - unnecessaryEntitiesCount).toFloat() / totalEntities.toFloat()

        return RuleEvalResult(RuleEvalScore(score, ScoreType.PERCENTAGE), messages, rule.ruleType, rule.id)


    }

}
