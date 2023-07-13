package com.wipdev.eWiLL_backend.eval.rules


import com.wipdev.eWiLL_backend.eval.compile.DiagramEvalPL

interface IRuleEvaluator {

    fun eval(diagramEvalPL: DiagramEvalPL, rule: Rule): RuleEvalResult

}