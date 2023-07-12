package com.wipdev.eWiLL_backend.utils.stringsimmilarity

import kotlin.math.max
import kotlin.math.min

class StringFinderUtils {

    companion object {
        fun isPresent(
            text: String,
            possibles: Array<String>,
            matchingValue: Double = 1.0,
            ignoreCase: Boolean = false
        ): Boolean {
            for (possible in possibles) {
                val case = if (ignoreCase) text.lowercase() else text
                val textCase = if (ignoreCase) possible.lowercase() else possible
                if (isPresent(textCase, case, matchingValue)) {
                    return true
                }
            }
            return false
        }

        fun getPresent(
            text: String,
            possibles: Array<String>,
            matchingValue: Double = 1.0,
            ignoreCase: Boolean = false
        ): String? {
            for (possible in possibles) {
                if (isPresent(text, possible, matchingValue)) {
                    return possible
                }
            }
            return null
        }

        private fun isPresent(text: String, possible: String, matchingValue: Double): Boolean {
            return getSimilarity(text, possible) >= matchingValue
        }

        fun getSimilarity(text: String, possible: String): Double {
            return findSimilarity(text, possible)
        }

        private fun getLevenshteinDistance(X: String, Y: String): Int {
            val m = X.length
            val n = Y.length
            val T = Array(m + 1) { IntArray(n + 1) }
            for (i in 1..m) {
                T[i][0] = i
            }
            for (j in 1..n) {
                T[0][j] = j
            }
            var cost: Int
            for (i in 1..m) {
                for (j in 1..n) {
                    cost = if (X[i - 1] == Y[j - 1]) 0 else 1
                    T[i][j] = min(
                        min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                        T[i - 1][j - 1] + cost
                    )
                }
            }
            return T[m][n]
        }

        private fun findSimilarity(x: String?, y: String?): Double {
            require(!(x == null || y == null)) { "Strings should not be null" }

            val maxLength = max(x.length, y.length)
            return if (maxLength > 0) {
                (maxLength * 1.0 - getLevenshteinDistance(x, y)) / maxLength * 1.0
            } else 1.0
        }

    }
}