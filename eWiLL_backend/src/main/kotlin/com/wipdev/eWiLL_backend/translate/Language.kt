package com.wipdev.eWiLL_backend.translate

import java.util.*

enum class Language {
    German("DE"),English("EN"),None("NONE");

    private val languageCode : String

    constructor(languageCode : String){
        this.languageCode = languageCode
    }

    fun getLanguageCode(): String {
        return languageCode
    }
    companion object {
        fun getByLanguageCode(languageCode: String): Language {
            val upperCase = languageCode.uppercase(Locale.ROOT)
            for (language in values()) {
                if (language.languageCode == upperCase) {
                    return language
                }
            }
            return None
        }
    }


}