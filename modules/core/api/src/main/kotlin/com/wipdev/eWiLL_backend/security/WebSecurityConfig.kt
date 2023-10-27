package com.wipdev.eWiLL_backend.security

import com.wipdev.eWiLL_backend.services.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig {

    @Autowired
    lateinit var userDetailsServiceImpl: UserDetailsServiceImpl

    @Autowired
    var unauthorizedHandler: AuthEntryPointJwt? = null

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter()
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .anyRequest().permitAll()
        http.headers().frameOptions().disable()
        return http.build()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl)
        authenticationProvider.setPasswordEncoder(passwordEncoder())

        return authenticationProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        //TODO: return BCryptPasswordEncoder()
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    fun authenticationManagerBean(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}
