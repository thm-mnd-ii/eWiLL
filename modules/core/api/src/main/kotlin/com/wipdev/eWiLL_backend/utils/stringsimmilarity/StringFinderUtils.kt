package com.wipdev.eWiLL_backend.utils.stringsimmilarity

class StringFinderUtils {

    companion object{
        fun isPresent(text:String,possibles:Array<String>, matchingValue : Double = 1.0,ignoreCase : Boolean = false):Boolean{
            for (possible in possibles){
                val case = if (ignoreCase) text.lowercase() else text
                val textCase = if (ignoreCase) possible.lowercase() else possible
                if (isPresent(textCase,case,matchingValue)){
                    return true
                }
            }
            return false
        }
        fun getPresent(text:String,possibles:Array<String>, matchingValue : Double = 1.0,ignoreCase: Boolean = false):String?{
            for (possible in possibles){
                if (isPresent(text,possible,matchingValue)){
                    return possible
                }
            }
            return null
        }
        private fun isPresent(text:String, possible:String, matchingValue : Double):Boolean{
            return getSimilarity(text,possible) >= matchingValue
        }
        fun getSimilarity(text:String, possible:String):Double{
           //Check in percentage how much both strings are simmilar
            val textLength = text.length
            val possibleLength = possible.length
            val maxLength = if (textLength > possibleLength) textLength else possibleLength
            var matches = 0
            for(i in text.indices){
                if (possible.contains(text[i])){
                    matches++
                }
            }
            return matches.toDouble()/maxLength.toDouble()
        }

    }
}