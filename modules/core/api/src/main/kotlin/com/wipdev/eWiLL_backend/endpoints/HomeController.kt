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
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var authentificationManager: AuthenticationManager

    /**
     * This method is responsible for routing request to return index.html from a Cross Origin Request to build into the IFrame
     */
    @GetMapping("/")
    fun home(response:HttpServletResponse): String? {
        return "index.html"
    }

    @PostMapping("/tokenLogin")
    fun tokenLogin(@RequestParam("jsessionid") jsessionid: String?,request: HttpServletRequest,response: HttpServletResponse):ResponseEntity<JwtResponse>{
        if(jsessionid!=null){
            val jwtUtils = JwtUtils()
            val fbsTokenDecodingResult = JwtUtils.decodeFBSToken(jsessionid)
            val username = fbsTokenDecodingResult.username

            if (!userRepository.existsByUsername(username)) {
                createUserData("Bearer $jsessionid")
            }


            val authentication = authentificationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, "")
            )
            SecurityContextHolder.getContext().authentication = authentication
            val jwt = jwtUtils.generateJwtToken(authentication, fbsTokenDecodingResult.userID, username)
            val userDetails = authentication.principal as UserDetailsImpl
            val roles = userDetails.authorities.map { it.authority }
            return ResponseEntity.ok(JwtResponse(jwt, userDetails.id, userDetails.username, userDetails.email, roles))
        }else{
            return ResponseEntity.badRequest().build()
        }
    }


    private fun createUserData(authHeader : String) {
        val role = roleRepository.getReferenceById(ERole.ROLE_USER.ordinal.toLong())
        val fbsClient = FbsClient()
        val fbsUser = fbsClient.getUserInformation(
            authHeader,
            null//TODO: X-Forwarded-For Header is missing
        )
        val user = User(null, fbsUser.username!!,fbsUser.email!!,fbsUser.prename!!,fbsUser.surname!!, setOf(role))
        userRepository.save(user)
    }

    @GetMapping("{_:^(?!index\\.html|api).*\$}")
    fun index(): String = "index.html"
}