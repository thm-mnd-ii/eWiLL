package com.wipdev.eWiLL_backend.eval.utils

class Dictionary {

    companion object{
        fun getPossibleNames(name: String): List<String> {
            var list = mutableListOf<String>()
            list.add(name)
            //TODO implement working dictionary

            return list

        }

        fun getPlural(name: String): String {
            return name + "s"
        }

        fun getSingular(name: String): String {
            return name.substring(0,name.length-1)
        }


    }
}