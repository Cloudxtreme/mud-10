package uk.co.grahamcox.mud.server.telnet.ui.renderer

import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.junit.Assert
import org.junit.Test
import uk.co.grahamcox.mud.server.telnet.ui.UIConfigOption

/**
 * Unit tests for the Composite Renderer Factory
 */
class CompositeRendererFactoryTest : EasyMockSupport() {
    /**
     * Test that if the first factory creates a renderer then none of the others are called
     */
    @Test
    fun firstWorks() {
        val configOptions = mapOf<Class<*>, UIConfigOption>()
        val delegates = arrayListOf<RendererFactory>()
        val renderer = createMock(Renderer::class.java)

        for (i in 0..10) {
            delegates.add(createMock("firstWorks${i}", RendererFactory::class.java))
        }
        EasyMock.expect(delegates[0].createRenderer(configOptions))
                .andReturn(renderer)

        replayAll()

        val composite = CompositeRendererFactory(delegates)
        val created = composite.createRenderer(configOptions)

        verifyAll()
        Assert.assertSame(renderer, created)
    }

    /**
     * Test that if the first few factories can't create a renderer then we keep going until one does
     */
    @Test
    fun thirdWorks() {
        val configOptions = mapOf<Class<*>, UIConfigOption>()
        val delegates = arrayListOf<RendererFactory>()
        val renderer = createMock(Renderer::class.java)

        for (i in 0..10) {
            delegates.add(createMock("thirdWorks${i}", RendererFactory::class.java))
        }
        EasyMock.expect(delegates[0].createRenderer(configOptions))
                .andReturn(null)
        EasyMock.expect(delegates[1].createRenderer(configOptions))
                .andReturn(null)
        EasyMock.expect(delegates[2].createRenderer(configOptions))
                .andReturn(renderer)

        replayAll()

        val composite = CompositeRendererFactory(delegates)
        val created = composite.createRenderer(configOptions)

        verifyAll()
        Assert.assertSame(renderer, created)
    }

    /**
     * Test that if the none of the factories can create a renderer then we return null
     */
    @Test
    fun noneWork() {
        val configOptions = mapOf<Class<*>, UIConfigOption>()
        val delegates = arrayListOf<RendererFactory>()

        for (i in 0..10) {
            val factory = createMock("noneWork${i}", RendererFactory::class.java)
            EasyMock.expect(factory.createRenderer(configOptions))
                    .andReturn(null)
            delegates.add(factory)
        }

        replayAll()

        val composite = CompositeRendererFactory(delegates)
        val created = composite.createRenderer(configOptions)

        verifyAll()
        Assert.assertNull(created)
    }
}
