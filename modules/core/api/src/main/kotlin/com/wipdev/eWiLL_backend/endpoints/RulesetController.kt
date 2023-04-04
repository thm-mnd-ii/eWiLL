package com.wipdev.eWiLL_backend.endpoints;

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.endpoints.payload.requests.RulesetPl
import com.wipdev.eWiLL_backend.services.RulesetService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ruleset")
@Tag(name = "Ruleset", description = "Ruleset for Evaluation")
class RulesetController {

        @Autowired
        lateinit var rulesetService: RulesetService

        @CrossOrigin
        @GetMapping("/{id}")
        @ResponseBody
        fun getRuleset(@PathVariable id:Long): RulesetPl {
            return rulesetService.getRuleset(id)
        }

        @CrossOrigin
        @GetMapping()
        @ResponseBody
        fun getAllRulesets(): List<RulesetPl> {
            return rulesetService.getAllRulesets()
        }

        @CrossOrigin
        @PostMapping()
        @ResponseBody
        fun createRuleset(@RequestBody ruleset: RulesetPl): RulesetPl {
            return rulesetService.createRuleset(ruleset)
        }

        @CrossOrigin
        @PutMapping("/{id}")
        @ResponseBody
        fun updateRuleset(@PathVariable id:Long, @RequestBody ruleset: RulesetPl): RulesetPl {
            return rulesetService.updateRuleset(id, ruleset)
        }

        @CrossOrigin
        @DeleteMapping("/{id}")
        @ResponseBody
        fun deleteRuleset(@PathVariable id:Long) {
            rulesetService.deleteRuleset(id)
        }





}


