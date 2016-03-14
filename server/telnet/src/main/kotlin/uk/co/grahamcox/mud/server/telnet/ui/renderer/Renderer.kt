package uk.co.grahamcox.mud.server.telnet.ui.renderer

/**
 * Interface describing a renderer, which is literally the way that the changes in the game are reflected to the client
 */
interface Renderer {

    /**
     * Handle when we've received an actual byte from the client
     * @param byte The byte that was received
     */
    fun receiveByte(byte: Byte)
}
