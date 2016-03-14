package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.junit.Assert
import org.junit.Test
import uk.co.grahamcox.mud.server.telnet.TelnetMessage
import uk.co.grahamcox.mud.server.telnet.options.EchoOption
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.options.SuppressGoAheadOption
import uk.co.grahamcox.mud.server.telnet.options.TelnetOption

/**
 * Unit tests for the Telnet Option Handler
 */
class TelnetOptionHandlerTest : EasyMockSupport() {
    /**
     * Test registering a new connection when there are no options to handle
     */
    @Test
    fun testRegisterOnConnectNoOptions() {
        val ctx = createMock(ChannelHandlerContext::class.java)

        EasyMock.expect(ctx.fireChannelRegistered())
                .andReturn(ctx)
        EasyMock.expect(ctx.flush())
                .andReturn(ctx)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(), serverOptions = mapOf())
        val handler = TelnetOptionHandler(optionManager)

        handler.channelRegistered(ctx)
        verifyAll()
    }

    /**
     * Test registering a new connection when there are some enabled client options
     */
    @Test
    fun testRegisterOnConnectEnabledClientOptions() {
        val ctx = createStrictMock(ChannelHandlerContext::class.java)

        EasyMock.expect(ctx.fireChannelRegistered())
                .andReturn(ctx)
        EasyMock.expect(ctx.write(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.WILL, 1)))
                .andReturn(null)
        EasyMock.expect(ctx.write(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.WILL, 3)))
                .andReturn(null)
        EasyMock.expect(ctx.flush())
                .andReturn(ctx)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(
                SuppressGoAheadOption() to OptionManager.InitialStatus.ENABLED,
                EchoOption() to OptionManager.InitialStatus.ENABLED
        ),
                serverOptions = mapOf())
        val handler = TelnetOptionHandler(optionManager)

        handler.channelRegistered(ctx)
        verifyAll()
    }

    /**
     * Test registering a new connection when there are some enabled client options
     */
    @Test
    fun testRegisterOnConnectDisabledClientOptions() {
        val ctx = createStrictMock(ChannelHandlerContext::class.java)

        EasyMock.expect(ctx.fireChannelRegistered())
                .andReturn(ctx)
        EasyMock.expect(ctx.flush())
                .andReturn(ctx)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(
                SuppressGoAheadOption() to OptionManager.InitialStatus.DISABLED,
                EchoOption() to OptionManager.InitialStatus.DISABLED
        ),
                serverOptions = mapOf())
        val handler = TelnetOptionHandler(optionManager)

        handler.channelRegistered(ctx)
        verifyAll()
    }

    /**
     * Test registering a new connection when there are some enabled server options
     */
    @Test
    fun testRegisterOnConnectEnabledServerOptions() {
        val ctx = createStrictMock(ChannelHandlerContext::class.java)

        EasyMock.expect(ctx.fireChannelRegistered())
                .andReturn(ctx)
        EasyMock.expect(ctx.write(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, 1)))
                .andReturn(null)
        EasyMock.expect(ctx.write(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, 3)))
                .andReturn(null)
        EasyMock.expect(ctx.flush())
                .andReturn(ctx)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(),
                serverOptions = mapOf(
                        SuppressGoAheadOption() to OptionManager.InitialStatus.ENABLED,
                        EchoOption() to OptionManager.InitialStatus.ENABLED
                ))
        val handler = TelnetOptionHandler(optionManager)

        handler.channelRegistered(ctx)
        verifyAll()
    }

    /**
     * Test registering a new connection when there are some disabled server options
     */
    @Test
    fun testRegisterOnConnectDisabledServerOptions() {
        val ctx = createStrictMock(ChannelHandlerContext::class.java)

        EasyMock.expect(ctx.fireChannelRegistered())
                .andReturn(ctx)
        EasyMock.expect(ctx.flush())
                .andReturn(ctx)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(),
                serverOptions = mapOf(
                        SuppressGoAheadOption() to OptionManager.InitialStatus.DISABLED,
                        EchoOption() to OptionManager.InitialStatus.DISABLED
                ))
        val handler = TelnetOptionHandler(optionManager)

        handler.channelRegistered(ctx)
        verifyAll()
    }

    /**
     * Test registering a new connection when there are a mixture of disabled and enabled server and client options
     */
    @Test
    fun testRegisterOnConnectMixedOptions() {
        val ctx = createStrictMock(ChannelHandlerContext::class.java)

        EasyMock.expect(ctx.fireChannelRegistered())
                .andReturn(ctx)
        EasyMock.expect(ctx.write(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, 1)))
                .andReturn(null)
        EasyMock.expect(ctx.write(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.WILL, 3)))
                .andReturn(null)
        EasyMock.expect(ctx.flush())
                .andReturn(ctx)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(
                SuppressGoAheadOption() to OptionManager.InitialStatus.ENABLED,
                EchoOption() to OptionManager.InitialStatus.DISABLED
        ),
                serverOptions = mapOf(
                        SuppressGoAheadOption() to OptionManager.InitialStatus.DISABLED,
                        EchoOption() to OptionManager.InitialStatus.ENABLED
                ))
        val handler = TelnetOptionHandler(optionManager)

        handler.channelRegistered(ctx)
        verifyAll()
    }

    /**
     * Test reading a negotiation for an option that we don't support
     */
    @Test
    fun testReadNegotiationUnknownOption() {
        val ctx = createStrictMock(ChannelHandlerContext::class.java)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(), serverOptions = mapOf())
        val handler = TelnetOptionHandler(optionManager)

        handler.channelRead(ctx, TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, 1))
        verifyAll()
    }

    /**
     * Test reading a negotiation for an option that we do support
     */
    @Test
    fun testReadNegotiationDisableClientOption() {
        val ctx = createStrictMock(ChannelHandlerContext::class.java)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(
                EchoOption() to OptionManager.InitialStatus.ENABLED
        ), serverOptions = mapOf())
        optionManager.getClientOption(EchoOption::class.java).serverState = TelnetMessage.NegotiationMessage.Negotiation.WILL

        val handler = TelnetOptionHandler(optionManager)

        handler.channelRead(ctx, TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DONT, 1))
        verifyAll()

        Assert.assertEquals(TelnetMessage.NegotiationMessage.Negotiation.DONT,
                optionManager.getClientOption(EchoOption::class.java).clientState)
        Assert.assertEquals(TelnetOption.OptionState.DISABLED,
                optionManager.getClientOption(EchoOption::class.java).state)
    }

    /**
     * Test reading a negotiation for an option that we do support
     */
    @Test
    fun testReadNegotiationEnableClientOption() {
        val ctx = createStrictMock(ChannelHandlerContext::class.java)

        replayAll()

        val optionManager = OptionManager(clientOptions = mapOf(
                EchoOption() to OptionManager.InitialStatus.ENABLED
        ), serverOptions = mapOf())
        optionManager.getClientOption(EchoOption::class.java).serverState = TelnetMessage.NegotiationMessage.Negotiation.WILL

        val handler = TelnetOptionHandler(optionManager)

        handler.channelRead(ctx, TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, 1))
        verifyAll()

        Assert.assertEquals(TelnetMessage.NegotiationMessage.Negotiation.DO,
                optionManager.getClientOption(EchoOption::class.java).clientState)
        Assert.assertEquals(TelnetOption.OptionState.ENABLED,
                optionManager.getClientOption(EchoOption::class.java).state)
    }
}