package com.wipdev.eWiLL_backend.utils.translate

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

//https://rapidapi.com/microsoft-azure-org-microsoft-cognitive-services/api/microsoft-translator-text
class Translator {

    companion object {

        fun translate(message: String, language: Language): Array<TranslateResponse>? {


            val client = WebClient.create()
            val bodyMessage = "[{\"Text\": \"$message\"}]";

            val response= client.post()
                .uri("https://microsoft-translator-text.p.rapidapi.com/translate?to=${language.getLanguageCode().lowercase()}&api-version=3.0&includeAlignment=false&profanityAction=NoAction&textType=plain")
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "1514e2e3ddmsh210680c47cd54fbp17d09djsn4a62399d382a")
                .header("X-RapidAPI-Host", "microsoft-translator-text.p.rapidapi.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyMessage)
                .retrieve().bodyToMono<Array<TranslateResponse>>().block()
            return response
        }
        fun detectLanguage(message: String): Language {
            val client = WebClient.create();
            val bodyMessage = "[{\"Text\": \"$message\"}]";
            val response = client.post()
                .uri("https://microsoft-translator-text.p.rapidapi.com/Detect?api-version=3.0")
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "1514e2e3ddmsh210680c47cd54fbp17d09djsn4a62399d382a")
                .header("X-RapidAPI-Host", "microsoft-translator-text.p.rapidapi.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyMessage)
                .retrieve().bodyToMono<Array<DetectLanguageResponse>>().block()

            return response!![0].language?.let { Language.getByLanguageCode(it) }!!
        }

    }
}