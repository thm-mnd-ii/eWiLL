package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.User
import com.wipdev.eWiLL_backend.endpoints.payload.requests.LoginRequestPL
import com.wipdev.eWiLL_backend.endpoints.payload.responses.JwtResponse
import com.wipdev.eWiLL_backend.repository.RoleRepository
import com.wipdev.eWiLL_backend.repository.UserRepository
import com.wipdev.eWiLL_backend.security.auth.*
import com.wipdev.eWiLL_backend.utils.fbs.FbsClient
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Description
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Auth API")
class AuthController {
    @Autowired
    lateinit var authentificationManager: AuthenticationManager

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var jwtUtils: JwtUtils


    @PostMapping("/signin")
    @Description("Sign in with username and password")
    @ResponseBody
    fun authenticateUser(
        @RequestBody loginRequestPL: LoginRequestPL,
        @Parameter(
            name = "X-Forwarded-For",
            `in` = ParameterIn.HEADER,
            description = "IP Address",
            required = true
        ) request: HttpServletRequest
    )
            : ResponseEntity<JwtResponse> {
        val fbsClient = FbsClient()
        val response = fbsClient.getLoginLdap(loginRequestPL.username!!, loginRequestPL.password!!, request)
        var fbsUser = FbsClient.FbsUser()
        if (response!!.statusCode() != 200) {
            if (response.statusCode() != 401)
                print(response.statusCode().toString() + " " + response.body())
            return ResponseEntity.status(response.statusCode()).build()
        } else {
            if (!userRepository.existsByUsername(loginRequestPL.username)) {
                fbsUser = fbsClient.getUserInformation(
                    response.headers().firstValue("Authorization").get(),
                    request.getHeader("X-Forwarded-For")
                )
                println(fbsUser)
                createUserDataFromAuthHeader(fbsUser)
            }
        }


        val authentication = authentificationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequestPL.username, "")
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication, fbsUser.id!!, fbsUser.username!!)
        val userDetails = authentication.principal as UserDetailsImpl
        val roles = userDetails.authorities.map { it.authority }
        return ResponseEntity.ok(JwtResponse(jwt, userDetails.id, userDetails.username, userDetails.email, roles))
    }

    private fun createUserDataFromAuthHeader(fbsUser: FbsClient.FbsUser) {
        val role = roleRepository.getReferenceById(ERole.ROLE_USER.ordinal.toLong())
        val user = User(null, fbsUser.username!!, fbsUser.email!!, fbsUser.prename!!, fbsUser.surname!!, setOf(role))
        userRepository.save(user)
    }

    @PostMapping("/tokenLogin")
    fun tokenLogin(@RequestParam("jsessionid") jsessionid: String?,request: HttpServletRequest,response: HttpServletResponse):ResponseEntity<JwtResponse>{
        if(jsessionid!=null){
            val jwtUtils = JwtUtils()
            val fbsTokenDecodingResult = JwtUtils.decodeFBSToken(jsessionid)
            val username = fbsTokenDecodingResult.username

            if (!userRepository.existsByUsername(username)) {
                createUserDataFromAuthHeader("Bearer $jsessionid")
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


    private fun createUserDataFromAuthHeader(authHeader : String) {
        val role = roleRepository.getReferenceById(ERole.ROLE_USER.ordinal.toLong())
        val fbsClient = FbsClient()
        val fbsUser = fbsClient.getUserInformation(
            authHeader,
            null//TODO: X-Forwarded-For Header is missing
        )
        val user = User(null, fbsUser.username!!,fbsUser.email!!,fbsUser.prename!!,fbsUser.surname!!, setOf(role))
        userRepository.save(user)
    }

    @GetMapping("/isValid")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    fun isValid(): Boolean {
        return true
    }


}