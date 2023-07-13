package com.wipdev.eWiLL_backend.endpoints

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class HomeController {
    @GetMapping("/")
    fun home(): String = "index.html"

    @GetMapping("/{path:[^api].*}")
    fun index(@PathVariable path: String): String = "index.html"
}