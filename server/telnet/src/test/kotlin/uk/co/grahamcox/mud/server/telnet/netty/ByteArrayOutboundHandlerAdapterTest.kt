package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.junit.Test

/**
 * Unit Tests for the Byte Array Outbound Handler Adapter
 */
class ByteArrayOutboundHandlerAdapterTest : EasyMockSupport() {

    /**
     * Test that when we see a ByteArray then we encode it into a series of bytes
     */
    @Test
    fun testEncodeByteArray() {
        val msg = (1..6).toList().map { it.toByte() }

        val ctx = createMock(ChannelHandlerContext::class.java)
        val inputPromise = createMock("InputPromise", ChannelPromise::class.java)
        val voidPromise = createMock("VoidPromise", ChannelPromise::class.java)

        EasyMock.expect(ctx.voidPromise())
                .andReturn(voidPromise)

        msg.dropLast(1).forEach { b ->
            val newPromise = createMock("NewPromise", ChannelPromise::class.java)
            EasyMock.expect(ctx.newPromise())
                    .andReturn(newPromise)
            EasyMock.expect(ctx.write(b, newPromise))
                    .andReturn(null)
        }
        EasyMock.expect(ctx.write(msg.last(), inputPromise))
                .andReturn(null)

        replayAll()

        val handler = ByteArrayOutboundHandlerAdapter()
        handler.write(ctx, msg.toByteArray(), inputPromise)

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

        val handler = ByteArrayOutboundHandlerAdapter()
        handler.write(ctx, msg, promise)

        verifyAll()
    }
}