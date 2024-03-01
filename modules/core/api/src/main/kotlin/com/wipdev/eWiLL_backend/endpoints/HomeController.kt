package com.wipdev.eWiLL_backend.endpoints


import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@Tag(
    name = "Routing Controller",
    description = "This controller is responsible for routing request to return index.html"
)
class HomeController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/")
    fun home(): String? {
        logger.info("Redirecting to / index.html")
        return "index.html"
    }

    @GetMapping("{_:^(?!index\\.html|api).*\$}")
    fun index(): String {
        logger.info("Redirecting to /**")
        return "forward:/"
    }

}