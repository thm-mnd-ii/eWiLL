package com.wipdev.eWiLL_backend.endpoints


import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
@Tag(name = "Routing Controller", description = "This controller is responsible for routing request to return index.html")
class HomeController {

    @GetMapping("/")
    fun home(): String? {
        return "index.html"
    }



    @GetMapping("{_:^(?!index\\.html|api).*\$}")
    fun index(): String = "index.html"
}