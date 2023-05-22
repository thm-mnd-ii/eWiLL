package com.wipdev.eWiLL_backend.eval.compile

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset

class DiagramEvalPL (
    var diagramData: DiagramData?,
    var ruleset: Ruleset?,
    var solutionDiagramData: List<DiagramData>?



){
    lateinit var bestSolutionDiagram : DiagramData
}