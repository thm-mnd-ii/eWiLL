package com.wipdev.eWiLL_backend.utils.translate

class Dictionary {


    companion object {
        private val defaultLanguage = Language.English//TODO get default language from user
        fun getPossibleNames(name: String): Array<String> {
            val list = mutableListOf<String>()
            list.add(name)
            Translator.translate(name, defaultLanguage)?.data!!.translations!!.translatedText?.let {
                list.add(it);list.addAll(
                getPlural(it)
            );list.add(getSingular(it))
            }

            //Add plurals and signulars
            list.addAll(getPlural(name))
            list.add(getSingular(name))

            return list.toTypedArray()

        }

        fun getPlural(name: String): Array<String> {
            return arrayOf(name + "s", name + "es", name + "ies", name + "e")
        }

        fun getSingular(name: String): String {
            return name.substring(0, name.length - 1)
        }


    }
}