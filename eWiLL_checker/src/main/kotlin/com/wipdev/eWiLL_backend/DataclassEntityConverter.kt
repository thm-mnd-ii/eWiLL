package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.database.tables.Diagram
import com.wipdev.eWiLL_backend.database.tables.Task


class DataclassEntityConverter {

    companion object {
        fun convert(task: Task): com.wipdev.eWiLL_backend.endpoints.payload.requests.Task {
            return com.wipdev.eWiLL_backend.endpoints.payload.requests.Task(
                task.id,
                task.name,
                task.description,
                task.dueDate,
                task.subject,
                task.courseId,
                task.solutionModelId,
                task.rulesetId
            )

        }


        fun convert(task: com.wipdev.eWiLL_backend.endpoints.payload.requests.Task): Task {
            return Task(
                task.id,
                task.name,
                task.description,
                task.dueDate,
                task.subject,
                task.courseId,
                task.solutionModelId,
                task.rulesetId
            )

        }

        fun convert(diagram: Diagram): com.wipdev.eWiLL_backend.endpoints.payload.requests.Diagram {
            return com.wipdev.eWiLL_backend.endpoints.payload.requests.Diagram(
                diagram.entities, diagram.ankerPoints, diagram.id
            )
        }

        fun convert(diagram: com.wipdev.eWiLL_backend.endpoints.payload.requests.Diagram): Diagram {
            return Diagram(diagram.entities, diagram.ankerPoints, diagram.modelId)
        }


    }

}