package com.wipdev.eWiLL_backend.endpoints.payload.requests

import com.wipdev.eWiLL_backend.eval.rules.RuleConfig

data class RulesetPl(var id: Long?,var name: String?,var  ruleConfigs: List<RuleConfig>?) {

}