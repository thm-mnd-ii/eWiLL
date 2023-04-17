package com.wipdev.eWiLL_backend.endpoints

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/")
    fun home(): String = "index.html"

    @GetMapping("/{path:[^api].*}")
    fun index(): String = "index.html" 
}