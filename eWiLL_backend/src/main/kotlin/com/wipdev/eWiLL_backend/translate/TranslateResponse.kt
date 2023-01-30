package com.wipdev.eWiLL_backend.translate

class TranslateResponse {
    //[{"detectedLanguage":{"language":"en","score":1.0},"translations":[{"text":"Hallo Welt","to":"de"}]}]
    var detectedLanguage: DetectedLanguage? = null

    class DetectedLanguage {
        var language: String? = null
        var score: Float = 0.toFloat()
    }

    var translations: Array<Translation>? = null

    class Translation {
        var text: String? = null
        var to: String? = null
    }

    fun getTranslatedText(): String? {
        return translations?.get(0)?.text
    }
}