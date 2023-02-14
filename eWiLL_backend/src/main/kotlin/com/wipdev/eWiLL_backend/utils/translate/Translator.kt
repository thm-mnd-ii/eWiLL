package com.wipdev.eWiLL_backend.utils.translate

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

//https://rapidapi.com/microsoft-azure-org-microsoft-cognitive-services/api/microsoft-translator-text
class Translator {

    companion object {
        private const val apiKey = "b16c46b90cmsh6b90f5a3bdee414p1c17cajsnd039ed0f8f81"

        fun translate(message: String, language: Language): Array<TranslateResponse>? {


            val client = WebClient.create()
            val bodyMessage = "[{\"Text\": \"$message\"}]";

            return client.post()
                .uri(
                    "https://microsoft-translator-text.p.rapidapi.com/translate?to=${
                        language.getLanguageCode().lowercase()
                    }&api-version=3.0&includeAlignment=false&profanityAction=NoAction&textType=plain"
                )
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "microsoft-translator-text.p.rapidapi.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyMessage)
                .retrieve().bodyToMono<Array<TranslateResponse>>().block()
        }
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