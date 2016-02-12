package uk.co.grahamcox.mud.server.telnet

/**
 * Representation of the current state of the Telnet Decoder
 */
sealed class TelnetDecoderState {
    /**
     * Wrapper around the response from injecting a byte into the decoder
     * @property newState The new state to transition into
     * @property message The message that was emitted, if any
     */
    data class InjectionResponse(val newState: TelnetDecoderState, val message: TelnetMessage? = null)

    /**
     * Inject a byte into the state and determine what happens with it
     * @param b The byte to inject
     * @return the response from injecting the byte
     *
     */
    abstract fun injectByte(b: Byte) : InjectionResponse

    /**
     * Representation of not being in a state at all
     */
    object NoState : TelnetDecoderState() {
        /**
         * When we receive an IAC Byte then we migrate to the IACState and don't emit anything
         * When we receive any other byte we remain in the NoState and we emit a ByteMessage
         * @param b The byte to check
         * @return the result of handling the byte
         */
        override fun injectByte(b: Byte) = when(b) {
            TelnetBytes.IAC -> InjectionResponse(IACState)
            else -> InjectionResponse(NoState, TelnetMessage.ByteMessage(b))
        }
    }

    /**
     * Representation of having just seen a bare IAC
     */
    object IACState : TelnetDecoderState() {
        /**
         * @param b The byte to check
         * @return the result of handling the byte
         */
        override fun injectByte(b: Byte) = InjectionResponse(NoState, TelnetMessage.ByteMessage(b))
    }

    /**
     * Representation of being in a Negotiation situation
     * @property negotiation The negotiation that is being performed
     */
    class NegotiationState(val negotiation: TelnetMessage.NegotiationMessage.Negotiation) : TelnetDecoderState() {
        /**
         * The byte we received is the Option ID that we are negotiating
         * @param b The byte to check
         * @return the result of handling the byte
         */
        override fun injectByte(b: Byte) = InjectionResponse(NoState, TelnetMessage.NegotiationMessage(negotiation, b))
    }

    /**
     * Representation of being in a Subnegotiation, having just received an IAC as the Payload
     * @property option The option being negotiated
     * @property payload The payload so far
     */
    class SubnegotiationIACState(val option: Byte, val payload: List<Byte>) : TelnetDecoderState() {
        /**
         * If the byte we have just received is an SE then we have finished subnegotiation. If not then it's part of the
         * payload after all
         * @param b The byte to check
         * @return the result of handling the byte
         */
        override fun injectByte(b: Byte) = when (b) {
            TelnetBytes.SE -> InjectionResponse(NoState, TelnetMessage.SubnegotiationMessage(option, payload))
            else -> InjectionResponse(SubnegotiationIACState(option, payload + b))
        }
    }
}
