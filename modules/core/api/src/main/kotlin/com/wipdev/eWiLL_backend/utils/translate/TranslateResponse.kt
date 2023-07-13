package com.wipdev.eWiLL_backend.utils.translate

class TranslateResponse {
    /*
    {
      "data": {
        "translations": {
          "translatedText": "¡Hola Mundo!"
        }
      }
    }
     */

    var data: Data? = null

    class Data {
        var translations: Translations? = null

        class Translations {
            var translatedText: String? = null
            override fun toString(): String {
                return "Translations(translated_text=$translatedText)"
            }
        }

        override fun toString(): String {
            return "Data(translations=$translations)"
        }
    }

    override fun toString(): String {
        return "TranslateResponse(data=$data)"
    }

}