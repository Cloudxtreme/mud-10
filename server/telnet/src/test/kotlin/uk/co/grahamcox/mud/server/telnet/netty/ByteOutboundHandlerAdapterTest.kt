package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.junit.Test
import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Unit Tests for the Byte Outbound Handler Adapter
 */
class ByteOutboundHandlerAdapterTest : EasyMockSupport() {

    /**
     * Test that when we see a Byte then we encode it into a ByteMessage
     */
    @Test
    fun testEncodeByte() {
        val b = 0x7f.toByte()

        val ctx = createMock(ChannelHandlerContext::class.java)
        val promise = createMock(ChannelPromise::class.java)

        EasyMock.expect(ctx.write(TelnetMessage.ByteMessage(b), promise))
                .andReturn(null)

        replayAll()

        val handler = ByteOutboundHandlerAdapter()
        handler.write(ctx, b, promise)

        verifyAll()
    }

    /**
     * Test that when we see a String then we don't encode it
     */
    @Test
    fun testEncodeOther() {
        val msg = "This is a String"

        val ctx = createMock(ChannelHandlerContext::class.java)
        val promise = createMock(ChannelPromise::class.java)

        EasyMock.expect(ctx.write(msg, promise))
                .andReturn(null)

        replayAll()

        val handler = ByteOutboundHandlerAdapter()
        handler.write(ctx, msg, promise)

        verifyAll()
    }
}