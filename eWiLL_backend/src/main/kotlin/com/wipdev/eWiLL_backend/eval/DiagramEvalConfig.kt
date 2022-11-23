package com.wipdev.eWiLL_backend.eval

enum class DiagramEvalConfig {

    COMPARE_ATTRIBUTES_BY_NAME,
    COMPARE_ENTITY_BY_NAME,
    COMPARE_ENTITY_BY_TYPE,
    COMPARE_ATTRIBUTES_BY_TYPE();

    val errorLevel:ErrorLevel
        get() {
            return when(this){
                COMPARE_ATTRIBUTES_BY_NAME -> ErrorLevel.INFO
                COMPARE_ENTITY_BY_NAME -> ErrorLevel.WARNING
                COMPARE_ENTITY_BY_TYPE -> ErrorLevel.WARNING
                COMPARE_ATTRIBUTES_BY_TYPE -> ErrorLevel.ERROR
            }
        }


    enum class ErrorLevel{
        INFO,WARNING,ERROR
    }

}