package com.wipdev.eWiLL_backend.endpoints


import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.services.DiagramService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/diagram")
@RestController
@Tag(name = "Diagram", description = "Diagram API")
class DiagramController {

    @Autowired
    lateinit var iDiagramService: DiagramService


    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = iDiagramService.getById(id)


    @PostMapping
    fun create(@RequestBody diagramPL: DiagramPL) = iDiagramService.create(diagramPL)


    @PutMapping
    fun update(@RequestBody diagramPL: DiagramPL) = diagramPL.id?.let { iDiagramService.update(it, diagramPL) }


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = iDiagramService.delete(id)

    //getall by user id

    @GetMapping("/user/{userId}")
    @ResponseBody
    fun getByUserId(@PathVariable userId: Long) = iDiagramService.getByUserId(userId)

}