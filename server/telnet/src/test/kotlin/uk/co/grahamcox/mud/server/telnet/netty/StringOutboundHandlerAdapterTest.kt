package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.junit.Assert
import org.junit.Test

/**
 * Unit Tests for the String Outbound Handler Adapter
 */
class StringOutboundHandlerAdapterTest : EasyMockSupport() {

    /**
     * Test that when we see a String then we encode it into a Byte Array
     */
    @Test
    fun testEncodeString() {
        val msg = "Hello"

        val ctx = createMock(ChannelHandlerContext::class.java)
        val promise = createMock(ChannelPromise::class.java)

        val capture = EasyMock.newCapture<ByteArray>()
        EasyMock.expect(ctx.write(EasyMock.capture(capture), EasyMock.same(promise)))
                .andReturn(null)

        replayAll()

        val handler = StringOutboundHandlerAdapter()
        handler.write(ctx, msg, promise)

        verifyAll()

        Assert.assertTrue(capture.hasCaptured())
        Assert.assertArrayEquals(msg.toByteArray(), capture.value)
    }

    /**
     * Test that when we see a Number then we don't encode it
     */
    @Test
    fun testEncodeOther() {
        val msg = 1

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