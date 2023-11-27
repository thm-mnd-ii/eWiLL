package com.wipdev.eWiLL_backend.security.auth


import com.auth0.jwt.JWT
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec


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


    fun validateJwtToken(jwtToken: String): Boolean {
        try {
           Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(jwtToken)
            return true
        } catch (e: Exception) {
            println("Invalid JWT signature: ${e.message}")
        }
        return false
    }



    companion object {


        fun decodeFBSToken(token: String?): FBSTokenDecodingResult {
            val decodedJWT = JWT.decode(token)
            return FBSTokenDecodingResult(
                decodedJWT.getClaim("id").asInt(),
                decodedJWT.getClaim("username").asString(),
                "",
                ""
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
            return if(keyString==null || keyString.isEmpty()){
                KeyGenerator.getInstance("HmacSHA256").generateKey()
            }else{
                Keys.hmacShaKeyFor(keyString.toByteArray())
            }
        }


    }

}