package com.wipdev.eWiLL_backend.endpoints;

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/location")
@Tag(name = "Location", description = "Location API")
class LocationController {

    @GetMapping
    @ResponseBody
    fun getLocation(): List<String> {
        return listOf("Friedberg", "Gießen", "Wetzlar")
    }
}
