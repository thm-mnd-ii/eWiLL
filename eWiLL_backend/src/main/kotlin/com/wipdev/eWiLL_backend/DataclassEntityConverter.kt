package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.database.tables.Task
import com.wipdev.eWiLL_backend.database.tables.Model


class DataclassEntityConverter {

    companion object{
        fun convert(task: Task): com.wipdev.eWiLL_backend.endpoints.dataclasses.Task {
            return com.wipdev.eWiLL_backend.endpoints.dataclasses.Task(task.id,task.name,task.description,task.dueDate,task.subject,task.teacher);
        }



        fun convert(task: com.wipdev.eWiLL_backend.endpoints.dataclasses.Task): Task {
            return Task(task.id,task.name,task.description,task.dueDate,task.subject,task.teacher);
        }

        fun convert(model: Model): com.wipdev.eWiLL_backend.endpoints.dataclasses.Model {
            return com.wipdev.eWiLL_backend.endpoints.dataclasses.Model(model.entities,model.ankerPoints,model.id);
        }

        fun convert(model: com.wipdev.eWiLL_backend.endpoints.dataclasses.Model): Model {
            return Model(model.entities,model.ankerPoints,model.modelId);
        }




    }

}