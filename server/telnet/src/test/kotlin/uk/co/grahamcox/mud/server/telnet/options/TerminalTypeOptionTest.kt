package uk.co.grahamcox.mud.server.telnet.options

import io.netty.channel.socket.SocketChannel
import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.junit.Assert
import org.junit.Test
import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Unit tests for the terminal type
 */
class TerminalTypeOptionTest : EasyMockSupport() {
    @Test
    fun testOptionEnabled() {
        val channel = createMock(SocketChannel::class.java)

        EasyMock.expect(channel.writeAndFlush(TelnetMessage.SubnegotiationMessage(24, listOf(1))))
                .andReturn(null)

        replayAll()

        val option = TerminalTypeOption(channel)
        option.clientState = TelnetMessage.NegotiationMessage.Negotiation.DO
        option.serverState = TelnetMessage.NegotiationMessage.Negotiation.WILL

        verifyAll()
    }

    /**
     * Test decoding the first terminal type
     */
    @Test
    fun testDecodeFirstTerminalType() {
        val channel = createMock(SocketChannel::class.java)

        EasyMock.expect(channel.writeAndFlush(TelnetMessage.SubnegotiationMessage(24, listOf(1))))
                .andReturn(null)

        replayAll()

        val option = TerminalTypeOption(channel)

        option.receiveSubnegotiation(listOf(0.toByte()) + "screen".toByteArray().toList())

        verifyAll()

        Assert.assertNull(option.terminalName)
    }

    /**
     * Test decoding the same terminal type twice
     */
    @Test
    fun testDecodeLastTerminalType() {
        val channel = createMock(SocketChannel::class.java)

        EasyMock.expect(channel.writeAndFlush(TelnetMessage.SubnegotiationMessage(24, listOf(1))))
                .andReturn(null)
                .times(2)

        replayAll()

        val option = TerminalTypeOption(channel)

        option.receiveSubnegotiation(listOf(0.toByte()) + "screen".toByteArray().toList())
        option.receiveSubnegotiation(listOf(0.toByte()) + "screen".toByteArray().toList())

        verifyAll()

        Assert.assertNull(option.terminalName)
    }

    /**
     * Test decoding the same terminal type three times
     */
    @Test
    fun testDecodeRealTerminalType() {
        val channel = createMock(SocketChannel::class.java)

        EasyMock.expect(channel.writeAndFlush(TelnetMessage.SubnegotiationMessage(24, listOf(1))))
                .andReturn(null)
                .times(2)

        replayAll()

        val option = TerminalTypeOption(channel)

        option.receiveSubnegotiation(listOf(0.toByte()) + "screen".toByteArray().toList())
        option.receiveSubnegotiation(listOf(0.toByte()) + "screen".toByteArray().toList())
        option.receiveSubnegotiation(listOf(0.toByte()) + "screen".toByteArray().toList())

        verifyAll()

        Assert.assertEquals("screen", option.terminalName)
    }

    /**
     * Test decoding the three different terminal types
     */
    @Test
    fun testDecodeDifferentTerminalTypes() {
        val channel = createMock(SocketChannel::class.java)

        EasyMock.expect(channel.writeAndFlush(TelnetMessage.SubnegotiationMessage(24, listOf(1))))
                .andReturn(null)
                .times(3)

        replayAll()

        val option = TerminalTypeOption(channel)

        option.receiveSubnegotiation(listOf(0.toByte()) + "screen1".toByteArray().toList())
        option.receiveSubnegotiation(listOf(0.toByte()) + "screen2".toByteArray().toList())
        option.receiveSubnegotiation(listOf(0.toByte()) + "screen3".toByteArray().toList())

        verifyAll()

        Assert.assertNull(option.terminalName)
    }
}
