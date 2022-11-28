package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.database.tables.Task
import com.wipdev.eWiLL_backend.database.tables.Diagram


class DataclassEntityConverter {

    companion object{
        fun convert(task: Task): com.wipdev.eWiLL_backend.endpoints.dataclasses.Task {
            return com.wipdev.eWiLL_backend.endpoints.dataclasses.Task(task.id,task.name,task.description,task.dueDate,task.subject,task.courseId);
        }



        fun convert(task: com.wipdev.eWiLL_backend.endpoints.dataclasses.Task): Task {
            return Task(task.id,task.name,task.description,task.dueDate,task.subject,task.teacher);
        }

        fun convert(diagram: Diagram): com.wipdev.eWiLL_backend.endpoints.dataclasses.Diagram {
            return com.wipdev.eWiLL_backend.endpoints.dataclasses.Diagram(diagram.entities,diagram.ankerPoints,diagram.id);
        }

        fun convert(diagram: com.wipdev.eWiLL_backend.endpoints.dataclasses.Diagram): Diagram {
            return Diagram(diagram.entities,diagram.ankerPoints,diagram.modelId);
        }




    }

}