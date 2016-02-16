package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.UnpooledByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.junit.Assert
import org.junit.Test
import uk.co.grahamcox.mud.server.telnet.TelnetBytes
import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Unit test for the Telnet Message Encoder
 */
class TelnetMessageEncoderTest : EasyMockSupport() {
    /**
     * Test encoding a Negotiation Message
     */
    @Test
    fun testEncodeNegotiation() {
        val ctx = createMock(ChannelHandlerContext::class.java)
        val promise = createMock(ChannelPromise::class.java)

        val allocator = UnpooledByteBufAllocator(false)

        val byteBufCapture = EasyMock.newCapture<ByteBuf>()

        EasyMock.expect(ctx.alloc())
            .andReturn(allocator)
        EasyMock.expect(ctx.write(EasyMock.capture(byteBufCapture), EasyMock.same(promise)))
            .andReturn(null)

        replayAll()

        val encoder = TelnetMessageEncoder()
        val message = TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, 99)
        encoder.write(ctx, message, promise)

        verifyAll()

        Assert.assertTrue(byteBufCapture.hasCaptured())
        val byteBuf = byteBufCapture.value
        Assert.assertEquals(3, byteBuf.readableBytes())
        Assert.assertEquals(TelnetBytes.IAC, byteBuf.readByte())
        Assert.assertEquals(TelnetBytes.DO, byteBuf.readByte())
        Assert.assertEquals(99.toByte(), byteBuf.readByte())
    }
}