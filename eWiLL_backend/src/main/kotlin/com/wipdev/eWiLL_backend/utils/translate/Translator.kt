package com.wipdev.eWiLL_backend.utils.translate

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

//https://rapidapi.com/gatzuma/api/deep-translate1
class Translator {

    companion object {
        private const val apiKey = "63b461c3c9msh9a6f64559704f0cp19464ejsn29c559948421"
        val host = "deep-translate1.p.rapidapi.com"
        val uri  = "https://deep-translate1.p.rapidapi.com/language/translate/v2"

        fun translate(message: String, language: Language):TranslateResponse? {


            val client = WebClient.create()
            var body = "{\"q\": \"$message\",\"target\": \"${language.getLanguageCode().lowercase()}\"}"

            return client.post()
                .uri(
                    uri
                )
                .header("content-type", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", host)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve().bodyToMono<TranslateResponse>().block()
        }
        @Deprecated("Update api")
        fun detectLanguage(message: String): Language {
            val client = WebClient.create();
            val bodyMessage = "[{\"Text\": \"$message\"}]";
            val response = client.post()
                .uri("https://microsoft-translator-text.p.rapidapi.com/Detect?api-version=3.0")
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "microsoft-translator-text.p.rapidapi.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyMessage)
                .retrieve().bodyToMono<Array<DetectLanguageResponse>>().block()

            return response!![0].language?.let { Language.getByLanguageCode(it) }!!
        }

    }
}