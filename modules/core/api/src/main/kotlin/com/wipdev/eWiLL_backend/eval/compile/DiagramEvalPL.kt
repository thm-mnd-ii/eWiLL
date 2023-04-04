package com.wipdev.eWiLL_backend.eval.compile

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL

class DiagramEvalPL (
    var diagramData: DiagramData?,
    var ruleset: Ruleset?,
    var solutionDiagramData: List<DiagramData>?



){
    lateinit var bestSolutionDiagram : DiagramData
}