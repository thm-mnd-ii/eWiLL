package com.wipdev.eWiLL_backend.utils.translate

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.core.util.Json
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

//https://rapidapi.com/gatzuma/api/deep-translate1
class Translator {

    companion object {
        private const val apiKey = "b3ef5a9604msh3b50f9b1b5eb6e1p14d124jsn0a0aa739d023"

        //private const val apiKey = "63b461c3c9msh9a6f64559704f0cp19464ejsn29c559948421"
        private const val host = "deep-translate1.p.rapidapi.com"
        private const val uri = "https://deep-translate1.p.rapidapi.com/language/translate/v2"

        private val cache: MutableMap<String, TranslateResponse> = mutableMapOf()

        private fun saveCacheToFile() {
            val file = java.io.File("cache.json")
            if (!file.exists()) {
                file.createNewFile()
            }
            file.writeText(Json.pretty(cache))
        }

        fun loadCacheFromFile() {
            val file = java.io.File("cache.json")
            if (!file.exists()) {
                file.createNewFile()

            }
            val text = file.readText()
            if (text.isEmpty()) return
            val objectMapper = ObjectMapper()
            val map: Map<String, TranslateResponse> = objectMapper.readValue(
                text,
                objectMapper.typeFactory.constructMapType(
                    Map::class.java,
                    String::class.java,
                    TranslateResponse::class.java
                )
            )
            cache.putAll(map)


        }

        fun translate(message: String, language: Language): TranslateResponse? {

            if (cache.containsKey(message)) {
                return cache[message]
            }
            val client = WebClient.create()
            val body = "{\"q\": \"$message\",\"target\": \"${language.getLanguageCode().lowercase()}\"}"

            val response = client.post()
                .uri(
                    uri
                )
                .header("content-type", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", host)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve().bodyToMono<TranslateResponse>().block()
            cache[message] = response!!
            saveCacheToFile()
            return response
        }

    }
}