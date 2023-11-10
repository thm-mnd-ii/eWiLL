package com.wipdev.eWiLL_backend.eval.rules.attributeequivalence

import com.wipdev.eWiLL_backend.endpoints.payload.requests.Attribute
import com.wipdev.eWiLL_backend.eval.FeedbackLevel
import com.wipdev.eWiLL_backend.eval.StatusLevel
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.*
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils
import com.wipdev.eWiLL_backend.utils.translate.Dictionary

class AttributeEquivalenceEvaluator : IRuleEvaluator {


    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule): RuleEvalResult {
        val messages = mutableListOf<ResultMessage>()
        var errors = 0
        for (entity in diagramEvalPL.bestSolutionDiagram.nodes) {
            for (attribute in entity.entity!!.attributes!!) {
                var possibleNames = Dictionary.getPossibleNames(attribute.name!!)
                if (attribute.name!!.lowercase() == "id" || attribute.name!!.lowercase() == "nr" || attribute.name!!.lowercase() == "number") {
                    possibleNames = arrayOf(
                        "id",
                        "ID",
                        "Id",
                        "iD",
                        "nr",
                        "Nr",
                        "NR",
                        "number",
                        "Number",
                        "NUMBER",
                        "id_",
                        "ID_",
                        "Id_",
                        "iD_",
                        "nr_",
                        "Nr_",
                        "NR_",
                        "number_",
                        "Number_",
                        "NUMBER_"
                    )
                }
                var found = false
                var foundAttribute: Attribute? = null
                if (entity.otherModelNode == null) {
                    messages.add(
                        ResultMessage(
                            FeedbackLevel.DEBUG,
                            "Skipping Entity ${entity.entity!!.entityName} cause it does not exist.",
                            entity.entity!!.id!!,
                            entity.getEntityName()!!,
                            "",
                            StatusLevel.INCORRECT,
                            ResultMessageType.Other
                        )
                    )
                    errors++
                    continue
                }
                for (att in entity.otherModelNode!!.entity!!.attributes!!) {
                    if (StringFinderUtils.isPresent(att.name!!, possibleNames)) {
                        messages.add(
                            ResultMessage(
                                FeedbackLevel.DEBUG,
                                "Attribute ${att.name} is correct",
                                entity.entity!!.id!!,
                                entity.getEntityName()!!,
                                att.name!!,
                                StatusLevel.CORRECT,
                                ResultMessageType.Attribute
                            )
                        )
                        found = true
                        foundAttribute = att
                    }
                }
                if (!found) {
                    messages.add(
                        ResultMessage(
                            FeedbackLevel.BASIC,
                            "Attribute ${attribute.name} is not found",
                            entity.entity!!.id!!,
                            entity.getEntityName()!!,
                            attribute.name!!,
                            StatusLevel.INCORRECT,
                            ResultMessageType.Attribute
                        )
                    )
                    messages.add(
                        ResultMessage(
                            FeedbackLevel.DEBUG,
                            "Possible names are: ${possibleNames.joinToString()}",
                            entity.entity!!.id!!,
                            entity.getEntityName()!!,
                            attribute.name!!,
                            StatusLevel.SUGGESTION,
                            ResultMessageType.Attribute
                        )
                    )
                    errors++
                } else {
                    var type: String = ""
                    if (attribute.pkey != foundAttribute!!.pkey || attribute.fkey != foundAttribute!!.fkey) {
                        if (foundAttribute!!.pkey && foundAttribute!!.fkey) {
                            type = "PrimaryKey/ForeignKey"
                        } else if (foundAttribute!!.pkey && !foundAttribute!!.fkey) {
                            type = "PrimaryKey"
                        } else if (!foundAttribute!!.pkey && foundAttribute!!.fkey) {
                            type = "ForeignKey"
                        }
                        messages.add(
                            ResultMessage(
                                FeedbackLevel.BASIC,
                                "Attribute ${attribute.name} has correct name but wrong type.",
                                entity.entity!!.id!!,
                                entity.getEntityName()!!,
                                attribute.name!!,
                                StatusLevel.INCORRECT,
                                ResultMessageType.Attribute
                            )
                        )
                        messages.add(
                            ResultMessage(
                                FeedbackLevel.DEBUG,
                                "Correct type is ${type}",
                                entity.entity!!.id!!,
                                entity.getEntityName()!!,
                                attribute.name!!,
                                StatusLevel.SUGGESTION,
                                ResultMessageType.Attribute
                            )
                        )
                        errors++
                    } else {
                        messages.add(
                            ResultMessage(
                                FeedbackLevel.DEBUG,
                                "Attribute ${attribute.name} has correct name and type.",
                                entity.entity!!.id!!,
                                entity.getEntityName()!!,
                                attribute.name!!,
                                StatusLevel.CORRECT,
                                ResultMessageType.Attribute
                            )
                        )
                    }
                }

            }

        }
        val totalAttributes = diagramEvalPL.bestSolutionDiagram.nodes.sumOf { it.entity!!.attributes!!.size }
        if (totalAttributes == 0) {
            messages.add(
                ResultMessage(
                    FeedbackLevel.DEBUG,
                    "Solution does not have Attributes",
                    0,
                    "",
                    "",
                    StatusLevel.INCORRECT,
                    ResultMessageType.Other
                )
            )
            return RuleEvalResult(RuleEvalScore(1f, ScoreType.PERCENTAGE), messages, rule.ruleType, rule.id)
        }
        return RuleEvalResult(
            RuleEvalScore(
                (totalAttributes - errors) / totalAttributes.toFloat(),
                ScoreType.PERCENTAGE
            ), messages, rule.ruleType, rule.id
        )
    }
}