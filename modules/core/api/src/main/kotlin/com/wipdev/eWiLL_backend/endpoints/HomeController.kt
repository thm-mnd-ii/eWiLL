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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
@Tag(name = "Routing Controller", description = "This controller is responsible for routing request to return index.html")
class HomeController {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var authentificationManager: AuthenticationManager

    @GetMapping("/")
    fun home(@RequestParam("jsessionid") jsessionid: String?,response:HttpServletResponse): String {
        if(jsessionid!=null){
            val jwtUtils = JwtUtils()
            val fbsTokenDecodingResult = JwtUtils.decodeFBSToken(jsessionid) //Is this Working?
            val username = fbsTokenDecodingResult.username
            if (!userRepository.existsByUsername(username)) {
                createUserData(fbsTokenDecodingResult)
            }




            val authentication = authentificationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, "")
            )
            SecurityContextHolder.getContext().authentication = authentication
            val jwt = jwtUtils.generateJwtToken(authentication)
            val userDetails = authentication.principal as UserDetailsImpl
            val roles = userDetails.authorities.map { it.authority }

            val jsessionidCookie = ResponseCookie.from("JSESSIONID", jwt).path("/").sameSite("None").secure(true).maxAge(3600 * 24).build()
            response.setHeader(HttpHeaders.SET_COOKIE, jsessionidCookie.toString())
        }
        return "index.html"
    }

    private fun createUserData(fbsUser: JwtUtils.Companion.FBSTokenDecodingResult) {
        val role = roleRepository.getReferenceById(ERole.ROLE_USER.ordinal.toLong())
        //val user = User(null, fbsUser.username!!, fbsUser.email!!, fbsUser.prename!!, fbsUser.surname!!, setOf(role)) //TODO How do i get the email, prename and surname?
        //userRepository.save(user)
    }

    @GetMapping("{_:^(?!index\\.html|api).*\$}")
    fun index(): String = "index.html"
}