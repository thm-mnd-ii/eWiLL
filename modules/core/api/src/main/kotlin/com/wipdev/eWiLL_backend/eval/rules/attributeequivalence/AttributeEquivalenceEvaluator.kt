package com.wipdev.eWiLL_backend.eval.rules.attributeequivalence

import com.wipdev.eWiLL_backend.endpoints.payload.requests.Attribute
import com.wipdev.eWiLL_backend.eval.HighlightLevel
import com.wipdev.eWiLL_backend.eval.ResultLevel
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.*
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils
import com.wipdev.eWiLL_backend.utils.translate.Dictionary

class AttributeEquivalenceEvaluator: IRuleEvaluator {


    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule): RuleEvalResult {
        var messages = mutableListOf<ResultMessage>()
        var errors = 0
        for(entity in diagramEvalPL.bestSolutionDiagram.nodes) {
            for (attribute in entity.entity!!.attributes!!) {
               var possibleNames = Dictionary.getPossibleNames(attribute.name!!)
                var found = false
                var foundAttibute: Attribute? = null
                if(entity.otherModelNode == null){
                    messages.add(ResultMessage(ResultLevel.DEBUG,"Skipping Entity ${entity.entity!!.entityName} cause it does not exist.",entity.entity!!.id!!,"",
                        HighlightLevel.INCORRECT,ResultMessageType.Other))
                    errors++
                    continue
                }
                for(att in entity.otherModelNode!!.entity!!.attributes!!){
                    if(StringFinderUtils.isPresent(att.name!!,possibleNames)){
                        messages.add(ResultMessage(ResultLevel.BASIC,"Attribute ${att.name} is correct",entity.entity!!.id!!,att.name!!,
                            HighlightLevel.CORRECT,ResultMessageType.Attribute))
                        found = true
                        foundAttibute = att
                    }
                }
                if(!found){
                    messages.add(ResultMessage(ResultLevel.BASIC,"Attribute ${attribute.name} is not correct",entity.entity!!.id!!,attribute.name!!,HighlightLevel.INCORRECT,ResultMessageType.Attribute))
                    messages.add(ResultMessage(ResultLevel.INFO,"Possible names are: ${possibleNames.joinToString()}",entity.entity!!.id!!,attribute.name!!,HighlightLevel.SUGGESTION,ResultMessageType.Attribute))
                    errors++
                }else{
                    //Check for type
                    if(attribute.type != foundAttibute!!.type){
                        messages.add(ResultMessage(ResultLevel.BASIC,"Attribute ${attribute.name} has correct name but wrong type.",entity.entity!!.id!!,attribute.name!!,HighlightLevel.INCORRECT,ResultMessageType.Attribute))
                        messages.add(ResultMessage(ResultLevel.INFO,"Correct type is ${foundAttibute.type}",entity.entity!!.id!!,attribute.name!!,HighlightLevel.SUGGESTION,ResultMessageType.Attribute))
                        errors++
                    }else{
                        messages.add(ResultMessage(ResultLevel.BASIC,"Attribute ${attribute.name} has correct name and type.",entity.entity!!.id!!,attribute.name!!,HighlightLevel.CORRECT,ResultMessageType.Attribute))
                    }
                }

            }

        }
        val totalAttributes = diagramEvalPL.bestSolutionDiagram.nodes.sumOf { it.entity!!.attributes!!.size }
        return RuleEvalResult(RuleEvalScore((totalAttributes-errors)/totalAttributes.toFloat(), ScoreType.PERCENTAGE),messages,rule.ruleType,rule.id)
    }
}