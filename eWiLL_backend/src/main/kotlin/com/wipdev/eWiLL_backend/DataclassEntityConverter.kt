package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.database.tables.Task
import com.wipdev.eWiLL_backend.database.tables.Diagram


class DataclassEntityConverter {

    companion object{
        fun convert(task: Task): com.wipdev.eWiLL_backend.endpoints.payload.Task {
            return com.wipdev.eWiLL_backend.endpoints.payload.Task(task.id,task.name,task.description,task.dueDate,task.subject,task.courseId,task.solutionModelId);
        }



        fun convert(task: com.wipdev.eWiLL_backend.endpoints.payload.Task): Task {
            return Task(task.id,task.name,task.description,task.dueDate,task.subject,task.courseId,task.solutionModelId);
        }

        fun convert(diagram: Diagram): com.wipdev.eWiLL_backend.endpoints.payload.Diagram {
            return com.wipdev.eWiLL_backend.endpoints.payload.Diagram(diagram.entities,diagram.ankerPoints,diagram.id);
        }

        fun convert(diagram: com.wipdev.eWiLL_backend.endpoints.payload.Diagram): Diagram {
            return Diagram(diagram.entities,diagram.ankerPoints,diagram.modelId);
        }




    }

}