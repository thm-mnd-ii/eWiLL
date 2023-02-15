package com.wipdev.eWiLL_backend.eval.rules

class RuleConfig(){
    var ruleId : Int? = null
    var config : Map<String,Any>? = null
    constructor(ruleId: Int?, config: Map<String, Any>?) : this() {
        this.ruleId = ruleId
        this.config = config
    }


}