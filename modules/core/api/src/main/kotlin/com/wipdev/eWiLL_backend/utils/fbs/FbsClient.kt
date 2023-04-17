package com.wipdev.eWiLL_backend.utils.fbs

import com.auth0.jwt.JWT
import com.wipdev.eWiLL_backend.security.auth.JwtUtils.Companion.FBSTokenDecodingResult
import io.swagger.v3.core.util.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class FbsClient {

    private final val baseUrl = "https://feedback.mni.thm.de/api/v1"

    fun getLoginLdap(username: String, password: String): HttpResponse<String>? {
        val url = "$baseUrl/login/ldap"
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"$username\",\"password\":\"$password\"}"))
            .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun getUserInformation(headers:HttpHeaders): FbsUser {
        val decodingResult = decodeFBSToken(headers.firstValue("Authorization").get().subSequence("Bearer ".length,headers.firstValue("Authorization").get().length).toString())
        val id = decodingResult.userID
        val url = "$baseUrl/users/${id}"
        val client = HttpClient.newBuilder().build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", headers.firstValue("Authorization").get())
            .GET()
            .build()
        val response =  client.send(request, HttpResponse.BodyHandlers.ofString())
        println(response.body() + " " + response.statusCode())
        return if(response.statusCode() != 200){
            FbsUser()
        }else{
            Json.mapper().readValue(response.body(), FbsUser::class.java)
        }

    }


    private fun decodeFBSToken(token: String?): FBSTokenDecodingResult {
        val decodedJWT = JWT.decode(token)
        return FBSTokenDecodingResult(
            decodedJWT.getClaim("id").asInt(),
            decodedJWT.getClaim("username").asString(),
            decodedJWT.getClaim("courseRoles").asString(),
            decodedJWT.getClaim("globalRole").asString(),
            "",
            ""
        )
    }

    class FbsUser{
        var id:Int? = 0
        var prename:String? = ""
        var surname:String? = ""
        var email:String? = ""
        var username:String? = ""
        var password:String? = ""
        var alias:String? = ""
        var globalRole:String? = ""
        var name : String? = ""

        override fun toString(): String {
            return "FbsUser(id=$id, prename='$prename', surname='$surname', email='$email', username='$username', password='$password', alias='$alias', globalRole='$globalRole', name='$name')"
        }
    }
}