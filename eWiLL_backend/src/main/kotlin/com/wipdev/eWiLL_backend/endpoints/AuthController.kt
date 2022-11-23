package com.wipdev.eWiLL_backend.endpoints

import com.wipdev.eWiLL_backend.database.tables.User
import com.wipdev.eWiLL_backend.endpoints.dataclasses.JwtResponse
import com.wipdev.eWiLL_backend.endpoints.dataclasses.LoginRequest
import com.wipdev.eWiLL_backend.repository.RoleRepository
import com.wipdev.eWiLL_backend.repository.UserRepository
import com.wipdev.eWiLL_backend.security.auth.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
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
    fun authenticateUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        //TODO: registerIfNonExistent(loginRequest)
        val authentication = authentificationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        val roles =  userDetails.authorities.map { it.authority }
        return ResponseEntity.ok(JwtResponse(jwt,userDetails.id, userDetails.username,userDetails.email, roles))
    }


    fun registerIfNonExistent(loginRequest: LoginRequest){
        if(userRepository.existsByUsername(loginRequest.username)){
            return
        }
        val user = User(loginRequest.username,loginRequest.password,loginRequest.username+"@thm.de", mutableSetOf(roleRepository.findByName(ERole.ROLE_USER.name)!!))
        userRepository.save(user)
    }






}