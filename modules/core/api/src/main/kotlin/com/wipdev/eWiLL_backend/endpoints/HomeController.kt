package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.User
import com.wipdev.eWiLL_backend.endpoints.payload.responses.JwtResponse
import com.wipdev.eWiLL_backend.repository.RoleRepository
import com.wipdev.eWiLL_backend.repository.UserRepository
import com.wipdev.eWiLL_backend.security.auth.ERole
import com.wipdev.eWiLL_backend.security.auth.JwtUtils
import com.wipdev.eWiLL_backend.security.auth.UserDetailsImpl
import com.wipdev.eWiLL_backend.utils.fbs.FbsClient
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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