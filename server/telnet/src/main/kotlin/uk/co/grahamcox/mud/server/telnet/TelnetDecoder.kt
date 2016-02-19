package uk.co.grahamcox.mud.server.telnet

import org.slf4j.LoggerFactory

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
        override fun injectByte(b: Byte) = when(b) {
            TelnetBytes.NOP -> InjectionResponse(NoState)
            TelnetBytes.DataMark ->
                InjectionResponse(NoState, TelnetMessage.CommandMessage(TelnetMessage.CommandMessage.Command.DATA_MARK))
            TelnetBytes.Break ->
                InjectionResponse(NoState, TelnetMessage.CommandMessage(TelnetMessage.CommandMessage.Command.BREAK))
            TelnetBytes.InterruptProcess ->
                InjectionResponse(NoState, TelnetMessage.CommandMessage(TelnetMessage.CommandMessage.Command.INTERRUPT_PROCESS))
            TelnetBytes.AbortOutput ->
                InjectionResponse(NoState, TelnetMessage.CommandMessage(TelnetMessage.CommandMessage.Command.ABORT_OUTPUT))
            TelnetBytes.AreYouThere ->
                InjectionResponse(NoState, TelnetMessage.CommandMessage(TelnetMessage.CommandMessage.Command.ARE_YOU_THERE))
            TelnetBytes.EraseCharacter ->
                InjectionResponse(NoState, TelnetMessage.CommandMessage(TelnetMessage.CommandMessage.Command.ERASE_CHARACTER))
            TelnetBytes.EraseLine ->
                InjectionResponse(NoState, TelnetMessage.CommandMessage(TelnetMessage.CommandMessage.Command.ERASE_LINE))
            TelnetBytes.GoAhead ->
                InjectionResponse(NoState, TelnetMessage.CommandMessage(TelnetMessage.CommandMessage.Command.GO_AHEAD))
            TelnetBytes.SB -> InjectionResponse(SubnegotiationOptionState)
            TelnetBytes.WILL-> InjectionResponse(NegotiationState(TelnetMessage.NegotiationMessage.Negotiation.WILL))
            TelnetBytes.WONT -> InjectionResponse(NegotiationState(TelnetMessage.NegotiationMessage.Negotiation.WONT))
            TelnetBytes.DO -> InjectionResponse(NegotiationState(TelnetMessage.NegotiationMessage.Negotiation.DO))
            TelnetBytes.DONT -> InjectionResponse(NegotiationState(TelnetMessage.NegotiationMessage.Negotiation.DONT))
            TelnetBytes.IAC -> InjectionResponse(NoState, TelnetMessage.ByteMessage(TelnetBytes.IAC))
            else -> InjectionResponse(NoState, TelnetMessage.ByteMessage(b))
        }
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
     * Representation of being in a Subnegotiation, about to receive the Option ID
     */
    object SubnegotiationOptionState : TelnetDecoderState() {
        /**
         * The next byte is the Option ID of the option we are sub-negotiating
         * @param b The byte to check
         * @return the result of handling the byte
         */
        override fun injectByte(b: Byte) =
                InjectionResponse(SubnegotiationPayloadState(b, listOf()))
    }
    /**
     * Representation of being in a Subnegotiation, whilst decoding the Payload
     * @property option The option being negotiated
     * @property payload The payload so far
     */
    class SubnegotiationPayloadState(val option: Byte, val payload: List<Byte>) : TelnetDecoderState() {
        /**
         * If the byte we have just received is an IAC then we need to move to the SubnegotiationIACState because
         * the next one might be the SE to end negotiation
         * @param b The byte to check
         * @return the result of handling the byte
         */
        override fun injectByte(b: Byte) = when (b) {
            TelnetBytes.IAC -> InjectionResponse(SubnegotiationIACState(option, payload))
            else -> InjectionResponse(SubnegotiationPayloadState(option, payload + b))
        }
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
            else -> InjectionResponse(SubnegotiationPayloadState(option, payload + b))
        }
    }
}

/**
 * The actual Decoder to decode telnet byte streams
 */
class TelnetDecoder {
    /** the logger to use */
    private val LOG = LoggerFactory.getLogger(TelnetDecoder::class.java)

    /** The current state of the decoder */
    private var currentState: TelnetDecoderState = TelnetDecoderState.NoState

    /**
     * Inject a byte into the decoder, and return the message produced if any
     * @param b The byte to inject
     * @return the produced message, if one was produced. Null if no message was produced
     */
    fun inject(b: Byte) : TelnetMessage? {
        val response = currentState.injectByte(b)
        LOG.trace("Injecting byte {} into state {} resulted in {}", b, currentState, response)
        currentState = response.newState
        return response.message
    }
}
