package com.wipdev.eWiLL_backend.eval

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.database.tables.Task
import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL

data class DiagramEvalEntry(val task:Task?,val ruleset: Ruleset?,val diagram: DiagramPL,val solutionDiagram:List<DiagramPL>?) {


}