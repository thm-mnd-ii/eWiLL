package com.wipdev.eWiLL_backend.security.auth


import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*


@Component
class JwtUtils {



    @Value("\${app.jwtExpirationMs}")
    private var jwtExpirationMs: Int = 0



    fun generateJwtToken(authentication: Authentication,id:Int,username:String): String {
        return Jwts.builder().setSubject("client_authentication")
            .claim("id", id)
            .claim("username", username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(getSecretKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUserNameFromJwtToken(token: String): String {
        return decodeFBSToken(token).username
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
           Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(authToken)
            return true
        } catch (e: Exception) {
            println("Invalid JWT signature: ${e.message}")
        }
        return false
    }



    companion object {
        fun decodeFBSToken(token: String?): FBSTokenDecodingResult {
            val claims:Claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).body
            return FBSTokenDecodingResult(
                claims["id"] as Int,
                claims["username"] as String,
                claims["exp"] as String,
                claims["iat"] as String
            )
        }


        class FBSTokenDecodingResult(
            val userID: Int,
            val username: String,
            val exp: String,
            val iat: String
        )

        fun getSecretKey(): Key {
            val keyString = System.getenv("JWT_SECRET")
            return if(keyString==null){
                io.jsonwebtoken.security.Keys.secretKeyFor(SignatureAlgorithm.HS256)
            }else{
                keyString.toByteArray().let { io.jsonwebtoken.security.Keys.hmacShaKeyFor(it) }
            }
        }

    }

}