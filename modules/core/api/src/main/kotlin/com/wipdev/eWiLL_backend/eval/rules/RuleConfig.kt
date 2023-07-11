package com.wipdev.eWiLL_backend.eval.rules

abstract class RuleConfig{
    var ruleId : Int? = null
    fun getConfig() : Map<String,Any>? = null
}