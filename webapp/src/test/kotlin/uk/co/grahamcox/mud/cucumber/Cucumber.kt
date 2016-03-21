package uk.co.grahamcox.mud.cucumber

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

/**
 * Runner for the Cucumber tests that are finished
 */
@RunWith(Cucumber::class)
@CucumberOptions(tags = arrayOf("~@wip", "~@ignore", "~@manual"),
        format = arrayOf("pretty",
                "html:target/site/cucumber/wip",
                "json:target/failsafe-reports/cucumberWip.json"),
        strict = true)
class CucumberIT


/**
 * Runner for the Cucumber tests that are still a work in progress finished
 */
@RunWith(Cucumber::class)
@CucumberOptions(tags = arrayOf("@wip", "~@ignore", "~@manual"),
        format = arrayOf("pretty",
                "html:target/site/cucumber/wip",
                "json:target/failsafe-reports/cucumberWip.json"),
        strict = false)
class CucumberWipIT
