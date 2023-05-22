package com.wipdev.eWiLL_backend.eval.rules.attributeequivalence

import com.wipdev.eWiLL_backend.endpoints.payload.requests.Attribute
import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL
import com.wipdev.eWiLL_backend.eval.rules.*
import com.wipdev.eWiLL_backend.utils.stringsimmilarity.StringFinderUtils
import com.wipdev.eWiLL_backend.utils.translate.Dictionary

class AttributeEquivalenceEvaluator: IRuleEvaluator {


    override fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule, ruleConfig: RuleConfig): RuleEvalResult {
        var stringBuilder = StringBuilder()
        var errors = 0
        for(entity in diagramEvalPL.bestSolutionDiagram.nodes) {
            for (attribute in entity.entity!!.attributes!!) {
               var possibleNames = Dictionary.getPossibleNames(attribute.name!!)
                var found = false
                var foundAttibute: Attribute? = null
                for(att in entity.otherModelNode.entity!!.attributes!!){
                    if(StringFinderUtils.isPresent(att.name!!,possibleNames)){
                        stringBuilder.append("Attribute ${att.name} is correct")
                        found = true
                        foundAttibute = att
                    }
                }
                if(!found){
                    stringBuilder.append("Attribute ${attribute.name} is not correct. Possible names are: ${possibleNames.joinToString()}")
                    errors++
                }else{
                    //Check for type
                    if(attribute.type != foundAttibute!!.type){
                        stringBuilder.append("Attribute ${attribute.name} has wrong type. Correct type is ${foundAttibute.type}")
                        errors++
                    }
                }

            }

        }
        return RuleEvalResult(RuleEvalScore(errors, ScoreType.ERROR_COUNT),stringBuilder.toString(),rule.ruleType,rule.id)
    }
}