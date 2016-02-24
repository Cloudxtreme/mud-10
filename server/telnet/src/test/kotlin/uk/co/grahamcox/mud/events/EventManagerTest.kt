package uk.co.grahamcox.mud.events

import org.junit.Assert
import org.junit.Test

/**
 * Unit tests for the Event Manager
 */
class EventManagerTest {
    /**
     * Test firing an event with no payload and a single listener
     */
    @Test
    fun testFireNoPayload() {
        val eventManager = EventManager()
        val events = arrayListOf<Event<*>>()

        eventManager.registerListener { e -> events.add(e) }

        eventManager.fire("event")

        Assert.assertEquals(1, events.size)
        Assert.assertEquals(Event("event", null), events[0])
    }

    /**
     * Test firing an event with a payload and a single listener
     */
    @Test
    fun testFirePayload() {
        val eventManager = EventManager()
        val events = arrayListOf<Event<*>>()

        eventManager.registerListener { e -> events.add(e) }

        eventManager.fire("event", 1)

        Assert.assertEquals(1, events.size)
        Assert.assertEquals(Event("event", 1), events[0])
    }

    /**
     * Test firing an event with multiple listeners
     */
    @Test
    fun testFireNoPayloadManyListeners() {
        val eventManager = EventManager()
        val events = arrayListOf<Event<*>>()

        for (i in 1..10) {
            eventManager.registerListener { e -> events.add(e) }
        }

        eventManager.fire("event")

        Assert.assertEquals(10, events.size)
        for (e in events) {
            Assert.assertEquals(Event("event", null), e)
            Assert.assertSame(events[0], e)
        }
    }

    /**
     * Test firing an event with a filter by name
     */
    @Test
    fun testFilterByName() {
        val eventManager = EventManager()
        val events = arrayListOf<Event<*>>()

        eventManager.registerListener("event") { e -> events.add(e) }
        eventManager.registerListener("other") { e -> events.add(e) }

        eventManager.fire("event")

        Assert.assertEquals(1, events.size)
        Assert.assertEquals(Event("event", null), events[0])
    }

    /**
     * Test firing an event with custom filter
     */
    @Test
    fun testCustomFilter() {
        val eventManager = EventManager()
        val events = arrayListOf<Event<*>>()

        for (i in 1..10) {
            eventManager.registerListener({ e -> events.isEmpty() }, { e -> events.add(e) })
        }

        eventManager.fire("event")

        Assert.assertEquals(10, events.size)
        for (e in events) {
            Assert.assertEquals(Event("event", null), e)
            Assert.assertSame(events[0], e)
        }
    }

}