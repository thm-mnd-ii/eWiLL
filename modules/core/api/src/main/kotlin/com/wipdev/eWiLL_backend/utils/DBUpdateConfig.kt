package com.wipdev.eWiLL_backend.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.wipdev.eWiLL_backend.services.DiagramService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class DBUpdateConfig : ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private lateinit var diagramService: DiagramService

    @Value("\${database.diagram.shouldUpdate}")
    private var shouldUpdate: Boolean = false

    private var objectMapper = ObjectMapper()

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (!shouldUpdate) {
            println("DBUpdateConfig skipped")
            return
        }
        println("DBUpdateConfig started")

        val diagrams = diagramService.findAll()
        val objectMapper = ObjectMapper()

        for (diagram in diagrams) {
            val entitiesJsonArray:List<JsonNode> = objectMapper.readValue(diagram.entities, object:
                TypeReference<List<JsonNode>>(){})


            val updatedEntities = entitiesJsonArray.map { entity ->
                val attributesArray = entity["attributes"]
                if (attributesArray != null && attributesArray.isArray) {
                    val updatedAttributesArray = attributesArray.map { attribute ->
                        val typeNode = attribute["type"]
                        if (typeNode != null && typeNode.isInt) {
                            val type = typeNode.asInt()

                            if (type in 0..2) {
                                val attributeNode = attribute as ObjectNode
                                attributeNode.remove("type")
                                attributeNode.put("pkey", type == 1)
                                attributeNode.put("fkey", type ==2)
                            }
                        }


                        attribute as JsonNode // Explicitly specify the type
                    }
                    (entity as ObjectNode).set<JsonNode>("attributes", objectMapper.valueToTree(updatedAttributesArray))
                }
                entity
            }

            // Convert the updated entities back to a JSON string
            val updatedEntitiesJson = objectMapper.writeValueAsString(updatedEntities)

            // Update the diagram with the new entities JSON
            diagram.entities = updatedEntitiesJson
            diagramService.update(diagram)
            println("Updated diagram with id ${diagram.id}")
        }

        println("DBUpdateConfig finished")
    }
}


