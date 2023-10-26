package com.wipdev.eWiLL_backend.endpoints

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
@Tag(name = "Routing Controller", description = "This controller is responsible for routing request to return index.html")
class HomeController {
    @GetMapping("/")
    fun home(@RequestParam("jsessionid") jsessionid: String?,response:HttpServletResponse): String {
        if(jsessionid!=null){
            val jsessionidCookie = ResponseCookie.from("JSESSIONID", jsessionid).path("/").sameSite("None").secure(true).maxAge(3600 * 24).build()


            response.setHeader(HttpHeaders.SET_COOKIE, jsessionidCookie.toString())
        }
        return "index.html"
    }

    @GetMapping("{_:^(?!index\\.html|api).*\$}")
    fun index(): String = "index.html"
}