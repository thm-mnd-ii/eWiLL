package com.wipdev.eWiLL_backend.utils.translate

class Dictionary {


    companion object {
        private val defaultLanguage = Language.English//TODO get default language from user
        fun getPossibleNames(name: String): Array<String> {
            var list = mutableListOf<String>()
            list.add(name)
            Translator.translate(name, defaultLanguage)?.data!!.translations!!.translatedText?.let { list.add(it);list.add(
                getPlural(it)
            );list.add(getSingular(it)) }

            //Add plurals and signulars
            list.add(getPlural(name))
            list.add(getSingular(name))

            return list.toTypedArray()

        }

        fun getPlural(name: String): String {
            return name + "s"
        }

        fun getSingular(name: String): String {
            return name.substring(0, name.length - 1)
        }


    }
}