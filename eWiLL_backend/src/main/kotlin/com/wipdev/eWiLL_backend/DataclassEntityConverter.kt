package com.wipdev.eWiLL_backend

import com.wipdev.eWiLL_backend.database.tables.Assignment
import com.wipdev.eWiLL_backend.database.tables.Model


class DataclassEntityConverter {

    companion object{
        fun convert(assignment: Assignment): com.wipdev.eWiLL_backend.endpoints.dataclasses.Assignment {
            return com.wipdev.eWiLL_backend.endpoints.dataclasses.Assignment(assignment.id,assignment.name,assignment.description,assignment.dueDate,assignment.subject,assignment.teacher);
        }



        fun convert(assignment: com.wipdev.eWiLL_backend.endpoints.dataclasses.Assignment): Assignment {
            return Assignment(assignment.id,assignment.name,assignment.description,assignment.dueDate,assignment.subject,assignment.teacher);
        }

        fun convert(model: Model): com.wipdev.eWiLL_backend.endpoints.dataclasses.Model {
            return com.wipdev.eWiLL_backend.endpoints.dataclasses.Model(model.entities,model.ankerPoints,model.id);
        }

        fun convert(model: com.wipdev.eWiLL_backend.endpoints.dataclasses.Model): Model {
            return Model(model.entities,model.ankerPoints,model.modelId);
        }




    }

}