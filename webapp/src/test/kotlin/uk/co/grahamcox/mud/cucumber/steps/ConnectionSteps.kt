package uk.co.grahamcox.mud.cucumber.steps

import cucumber.api.java.en.Given
import uk.co.grahamcox.mud.cucumber.facades.ConnectionFacade

/**
 * Cucumber steps to connect to the MUD
 */
class ConnectionSteps(private val connectionFacade: ConnectionFacade) {

    /**
     * Actually connect to the MUD
     */
    @Given("^I have connected to the MUD$")
    fun connect() {
        connectionFacade.connect()
    }
}
