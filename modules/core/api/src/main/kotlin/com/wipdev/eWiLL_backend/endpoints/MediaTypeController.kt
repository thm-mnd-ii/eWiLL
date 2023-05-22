package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.MediaType
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/mediaType")
@Tag(name = "Media Type", description = "Media Type API, a component of the Diagram API")
class MediaTypeController {

    @GetMapping()
    @ResponseBody
    fun getMediaTypes(): Array<MediaType> {
        return MediaType.values()
    }

    @GetMapping("/{id}")
    @ResponseBody
    fun getMediaTypeById(@PathVariable id: Int): MediaType {
        return MediaType.values()[id]
    }

}