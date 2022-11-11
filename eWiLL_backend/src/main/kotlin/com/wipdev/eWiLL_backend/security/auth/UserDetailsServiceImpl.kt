package com.wipdev.eWiLL_backend.security.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl: UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository
    override fun loadUserByUsername(username: String): UserDetails {
        var user: User = userRepository.findByUsername(username)!!
        return UserDetailsImpl.build(user)
    }
}