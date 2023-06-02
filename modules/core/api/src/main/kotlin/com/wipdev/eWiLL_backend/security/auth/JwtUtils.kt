package com.wipdev.eWiLL_backend.security.auth


import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*


@Component
class JwtUtils {

    private var jwtSecret: Key = io.jsonwebtoken.security.Keys.secretKeyFor(SignatureAlgorithm.HS512)

    @Value("\${app.jwtExpirationMs}")
    private var jwtExpirationMs: Int = 0

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl
        return Jwts.builder().setSubject(userPrincipal.username).setIssuedAt(Date()).setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith( jwtSecret,SignatureAlgorithm.HS512).compact()
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(authToken)
            return true
        } catch (e: Exception) {
            println("Invalid JWT signature: ${e.message}")
        }
        return false
    }

    companion object {
        fun decodeFBSToken(token: String?): FBSTokenDecodingResult {
            val decodedJWT: DecodedJWT = JWT.decode(token)
            return FBSTokenDecodingResult(
                decodedJWT.getClaim("id").asInt(),
                decodedJWT.getClaim("username").asString(),
                decodedJWT.getClaim("courseRoles").asString(),
                decodedJWT.getClaim("globalRole").asString(),
                decodedJWT.getClaim("exp").asString(),
                decodedJWT.getClaim("iat").asString()
            )
        }


        class FBSTokenDecodingResult(
            val userID: Int,
            val username: String,
            val courseRoles: String,
            val globalRole: String,
            val exp: String,
            val iat: String
        )


    }

}