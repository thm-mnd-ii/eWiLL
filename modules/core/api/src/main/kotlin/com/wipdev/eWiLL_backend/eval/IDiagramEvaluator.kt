package com.wipdev.eWiLL_backend.eval

interface IDiagramEvaluator {


    fun eval(diagramEvalEntry: DiagramEvalEntry): DiagramEvalResult
}