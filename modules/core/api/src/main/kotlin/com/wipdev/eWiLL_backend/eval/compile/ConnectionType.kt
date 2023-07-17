package com.wipdev.eWiLL_backend.eval.compile


enum class ConnectionType(var type: Int, var writtenName: String) {

    ZERO_TO_ONE(0,"Zero to One"),
    ZERO_TO_MANY(1,"Zero to Many"),
    ONE_TO_MANY(2,"One to Many"),
    ONE_TO_ONE(3,"One to One");

    companion object {
        private val map = ConnectionType.values().associateBy(ConnectionType::type)
        fun fromInt(type: Int) = map[type]
    }
}