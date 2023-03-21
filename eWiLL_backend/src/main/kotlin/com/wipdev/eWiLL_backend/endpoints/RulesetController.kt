package com.wipdev.eWiLL_backend.endpoints;

import com.wipdev.eWiLL_backend.database.tables.course.Ruleset
import com.wipdev.eWiLL_backend.services.IRulesetService
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
        fun getRuleset(@PathVariable id:Long): Ruleset {
            return rulesetService.getRuleset(id)
        }

        @CrossOrigin
        @GetMapping()
        @ResponseBody
        fun getAllRulesets(): List<Ruleset> {
            return rulesetService.getAllRulesets()
        }

        @CrossOrigin
        @PostMapping()
        @ResponseBody
        fun createRuleset(@RequestBody ruleset: Ruleset): Ruleset {
            return rulesetService.createRuleset(ruleset)
        }

        @CrossOrigin
        @PutMapping("/{id}")
        @ResponseBody
        fun updateRuleset(@PathVariable id:Long, @RequestBody ruleset: Ruleset): Ruleset {
            return rulesetService.updateRuleset(id, ruleset)
        }

        @CrossOrigin
        @DeleteMapping("/{id}")
        @ResponseBody
        fun deleteRuleset(@PathVariable id:Long) {
            rulesetService.deleteRuleset(id)
        }



}


