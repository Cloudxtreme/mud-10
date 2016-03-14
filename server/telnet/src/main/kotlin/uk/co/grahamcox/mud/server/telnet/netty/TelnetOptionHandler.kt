package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.TelnetMessage
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.options.TelnetOption

/**
 * Netty Handler that is responsible for handling all of the details of Telnet Option negotiations
 */
class TelnetOptionHandler(private val optionManager : OptionManager) : ChannelInboundHandlerAdapter() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(TelnetOptionHandler::class.java)
    /**
     * Handle when the client first connects, to go and request all of the options that we want to support
     * @param ctx The client context
     */
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        super.channelRegistered(ctx)
        optionManager.serverOptions
                .filter { option -> option.value == OptionManager.InitialStatus.ENABLED}
                .map { option -> option.key }
                .sortedBy { option -> option.optionId }
                .forEach { option -> negotationOption(ctx, option, TelnetMessage.NegotiationMessage.Negotiation.DO) }
        optionManager.clientOptions
                .filter { option -> option.value == OptionManager.InitialStatus.ENABLED}
                .map { option -> option.key }
                .sortedBy { option -> option.optionId }
                .forEach { option -> negotationOption(ctx, option, TelnetMessage.NegotiationMessage.Negotiation.WILL) }
        ctx.flush()
    }

    /**
     * Handle when an Option relevant message is received from the client
     * @param ctx The client context
     * @param msg The message
     */
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        when (msg) {
            is TelnetMessage.NegotiationMessage -> {
                val option = when (msg.negotiation) {
                    TelnetMessage.NegotiationMessage.Negotiation.DO -> optionManager.getClientOption(msg.option)
                    TelnetMessage.NegotiationMessage.Negotiation.DONT -> optionManager.getClientOption(msg.option)
                    TelnetMessage.NegotiationMessage.Negotiation.WILL -> optionManager.getServerOption(msg.option)
                    TelnetMessage.NegotiationMessage.Negotiation.WONT -> optionManager.getServerOption(msg.option)
                }

                if (option != null) {
                    option.clientState = msg.negotiation
                    LOG.debug("Received negotiation {} for option {}. New state is {}", msg, option, option.state)
                } else {
                    LOG.warn("Received negotiation for unknown option: {}", msg)
                }
            }
            is TelnetMessage.SubnegotiationMessage -> {
                // We don't know if it was a Client or Server option, so assume both
                optionManager.getClientOption(msg.option)?.let {
                    LOG.debug("Received subnegotiation {} for option {}", msg, it)
                    it.receiveSubnegotiation(msg.payload)
                }
                optionManager.getServerOption(msg.option)?.let {
                    LOG.debug("Received subnegotiation {} for option {}", msg, it)
                    it.receiveSubnegotiation(msg.payload)
                }
            }
            else -> super.channelRead(ctx, msg)
        }
    }

    /**
     * Actually send a negotiation for the given option
     * @param ctx The client context to send the message down
     * @param option The option to send the negotiation for
     * @param negotiation The negotiation to send
     */
    private fun negotationOption(ctx: ChannelHandlerContext, option: TelnetOption, negotiation: TelnetMessage.NegotiationMessage.Negotiation) {
        val msg = TelnetMessage.NegotiationMessage(negotiation, option.optionId)
        ctx.write(msg)
        option.serverState = negotiation
        LOG.debug("Sent negotiation {} for option {}. New state is {}", msg, option, option.state)
    }
}
