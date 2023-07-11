package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.ELiability
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/liability")
@Tag(name = "Liability", description = "Liability API")
class LiabilityController {


    @GetMapping
    @ResponseBody
    fun getLiability(): Array<ELiability> {
        return ELiability.values()
    }
}