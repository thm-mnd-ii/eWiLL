package com.wipdev.eWiLL_backend.services

import com.wipdev.eWiLL_backend.endpoints.payload.requests.SubmissionRequest
import com.wipdev.eWiLL_backend.repository.TaskRepository
import com.wipdev.eWiLL_backend.repository.DiagramRepository
import com.wipdev.eWiLL_backend.repository.RulesetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class EvaluationService:IEvaluationService {

    @Autowired
    lateinit var diagramRepository: DiagramRepository
    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var rulesetRepository: RulesetRepository
    override fun eval(submissionRequest: SubmissionRequest): String {
        TODO("Not yet implemented")
    }


    //IGNORE THIS OLD CODE
   /* override fun get(id: Long): Int {
        //get model from db
        var diagram: Diagram = diagramRepository.getReferenceById(id)
        var assignment = assignmentRepository.getReferenceById(diagram.assignmentId!!)
        var solutionModel = diagramRepository.getReferenceById(assignment.solutionModelId!!)
        //evaluate this model in comparison to the solution model

        var attributeConnections : MutableList<AttributeConnection> = getAttributeConnections(diagram)
        var solutionAttributeConnections : MutableList<AttributeConnection> = getAttributeConnections(solutionModel)

        return getScore(attributeConnections,solutionAttributeConnections, arrayOf(DiagramEvalConfig.COMPARE_ATTRIBUTES_BY_TYPE,DiagramEvalConfig.COMPARE_ENTITY_BY_TYPE))
    }

    private fun getScore(attributeConnections: MutableList<AttributeConnection>, solutionAttributeConnections: MutableList<AttributeConnection>,flags:Array<DiagramEvalConfig>): Int {
        //Check how many connections from solution are in model
        var score = 0
        val initialSize = solutionAttributeConnections.size;
        for (solutionConnection in solutionAttributeConnections){
            for (modelConnection in attributeConnections){
                if(solutionConnection.compare(modelConnection,flags)){
                    score++
                    solutionAttributeConnections.remove(solutionConnection)
                    attributeConnections.remove(modelConnection)
                }

            }
        }
        return score * 100/initialSize;
    }

    fun getAttributeConnections(diagram: Diagram) : MutableList<AttributeConnection>{
        var connections:MutableList<AttributeConnection> = mutableListOf()
        var ankers : String? = diagram.ankerPoints
        //parse json string
        val mapper = jacksonObjectMapper()
        var ankerPoints:Array<AnkerPoint> = mapper.readValue(diagram.ankerPoints!!)
        val entities : Array<Entity> = mapper.readValue(diagram.entities!!)

        for(anker in ankerPoints){
            var startEntityId = if (anker.startEntityPosition == "left") anker.startEntity else anker.endEntity
            var startEntity = entities.find { it.id == startEntityId }
            var endEntityId = if (anker.startEntityPosition == "left") anker.endEntity else anker.startEntity
            var endEntity = entities.find { it.id == endEntityId }
            for(startAttribute in startEntity!!.attributes){
                for(endAttribute in endEntity!!.attributes){
                    connections.add(AttributeConnection(startEntity.entityName,startAttribute.name,startEntity.typ,startAttribute.type,endEntity.entityName,endAttribute.name,endEntity.typ,endAttribute.type))
                }
            }
        }

        return connections
    }

    */
}