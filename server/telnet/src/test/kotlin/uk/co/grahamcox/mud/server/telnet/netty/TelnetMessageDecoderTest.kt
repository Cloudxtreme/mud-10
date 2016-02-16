package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.buffer.UnpooledByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.util.Attribute
import io.netty.util.AttributeKey
import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.junit.Test
import uk.co.grahamcox.mud.server.telnet.TelnetBytes
import uk.co.grahamcox.mud.server.telnet.TelnetDecoder
import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Unit tests for the Telnet Message Decoder
 */
class TelnetMessageDecoderTest : EasyMockSupport() {
    /**
     * Test decoding the bytes that represent a negotiation
     */
    @Test
    fun testDecodeNegotiation() {
        val ctx = createMock(ChannelHandlerContext::class.java)

        val attr = createMock(Attribute::class.java)

        EasyMock.expect(ctx.attr(EasyMock.anyObject<AttributeKey<*>>()))
            .andReturn(attr)

        EasyMock.expect(attr.get())
            .andReturn(TelnetDecoder())
            .times(2)
        EasyMock.expect(ctx.isRemoved)
            .andReturn(false)
        EasyMock.expect(ctx.fireChannelRead(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, 123)))
            .andReturn(ctx)

        replayAll()

        val allocator = UnpooledByteBufAllocator(false)
        val buffer = allocator.buffer()
        buffer.writeByte(TelnetBytes.IAC.toInt())
        buffer.writeByte(TelnetBytes.DO.toInt())
        buffer.writeByte(123)

        val decoder = TelnetMessageDecoder()
        decoder.channelRead(ctx, buffer)

        verifyAll()
    }
}