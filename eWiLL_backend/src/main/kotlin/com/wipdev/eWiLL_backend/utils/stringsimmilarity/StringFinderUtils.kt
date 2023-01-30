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
        private fun getSimilarity(text:String, possible:String):Double{
            val longer = if (text.length > possible.length) text else possible
            val shorter = if (text.length > possible.length) possible else text
            val longerLength = longer.length.toDouble()
            if (longerLength == 0.0) {
                return 1.0
            }
            return (longerLength - editDistance(longer, shorter)) / longerLength
        }
        private fun editDistance(s1: String, s2: String): Int {
            var costs: IntArray
            val n = s1.length
            var p: Int
            var m = s2.length
            var i: Int
            var j: Int
            var t_j: Int
            var cost: Int
            if (n == 0) {
                return m
            }
            if (m == 0) {
                return n
            }
            costs = IntArray(m + 1)
            i = 0
            while (i <= n) {
                costs[i] = i
                i++
            }
            j = 1
            while (j <= m) {
                costs[0] = j
                t_j = j - 1
                i = 1
                while (i <= n) {
                    cost = if (s1[i - 1] == s2[t_j]) 0 else 1
                    p = costs[i - 1] + 1
                    if (costs[i] + 1 < p) {
                        p = costs[i] + 1
                    }
                    if (costs[i - 1] + cost < p) {
                        p = costs[i - 1] + cost
                    }
                    costs[i - 1] = j
                    j = p
                    i++
                }
                costs[n] = j
                j = t_j + 1
            }
            return costs[n]
        }
    }
}