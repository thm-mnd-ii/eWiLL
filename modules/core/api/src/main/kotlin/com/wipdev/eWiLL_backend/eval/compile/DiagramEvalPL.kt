package com.wipdev.eWiLL_backend.eval.compile

class DiagramEvalPL(
    var diagramData: DiagramData?,
    var solutionDiagramData: List<DiagramData>?


) {
    lateinit var bestSolutionDiagram: DiagramData
}