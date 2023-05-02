package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.Role
import com.wipdev.eWiLL_backend.database.tables.User
import com.wipdev.eWiLL_backend.endpoints.payload.requests.LoginRequestPL
import com.wipdev.eWiLL_backend.endpoints.payload.responses.JwtResponse
import com.wipdev.eWiLL_backend.utils.fbs.FbsClient
import com.wipdev.eWiLL_backend.repository.RoleRepository
import com.wipdev.eWiLL_backend.repository.UserRepository
import com.wipdev.eWiLL_backend.security.auth.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

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
    @ResponseBody
    fun authenticateUser(@RequestBody loginRequestPL: LoginRequestPL): ResponseEntity<JwtResponse> {
        val fbsClient = FbsClient()
        val response = fbsClient.getLoginLdap(loginRequestPL.username!!, loginRequestPL.password!!)
        if (response!!.statusCode() != 200) {
            return ResponseEntity.status(response.statusCode()).build()
        } else {
            if (!userRepository.existsByUsername(loginRequestPL.username)) {
                val fbsUser = fbsClient.getUserInformation(response.headers())
                createUserData(fbsUser)
            }
        }


        val authentication = authentificationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequestPL.username, "")
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        val roles = userDetails.authorities.map { it.authority }
        return ResponseEntity.ok(JwtResponse(jwt, userDetails.id, userDetails.username, userDetails.email, roles))
    }

    private fun createUserData(fbsUser: FbsClient.FbsUser) {
        val role = roleRepository.getReferenceById(ERole.ROLE_USER.ordinal.toLong())
        val user = User(null,fbsUser.username!!, fbsUser.email!!,fbsUser.prename!!,fbsUser.surname!!, setOf(role))
        userRepository.save(user)
    }


    @GetMapping("/isValid")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    fun isValid(): Boolean {
        return true
    }


}