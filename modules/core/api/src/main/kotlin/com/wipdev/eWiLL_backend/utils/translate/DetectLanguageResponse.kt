package com.wipdev.eWiLL_backend.utils.translate

//[{"language":"en","score":1.0,"isTranslationSupported":true,"isTransliterationSupported":false}]
class DetectLanguageResponse {
    private var language: String? = null
    private var score: Float = 0.toFloat()
    private var isTranslationSupported: Boolean = false
    private var isTransliterationSupported: Boolean = false

    override fun toString(): String {
        return "DetectLanguageResponse{" +
                "language='" + language + '\''.toString() +
                ", score=" + score +
                ", isTranslationSupported=" + isTranslationSupported +
                ", isTransliterationSupported=" + isTransliterationSupported +
                '}'.toString()
    }
}