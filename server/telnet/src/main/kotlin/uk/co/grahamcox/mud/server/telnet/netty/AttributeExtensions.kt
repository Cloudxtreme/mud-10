package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.util.Attribute

/**
 * Extension method on the Netty Attribute to get the value of an Attribute, setting it
 * if it doesn't already have a value. This version takes a lambda to produce the value
 * to set the attribute to if it doesn't already have a value
 * @param generator The lambda to generate the initial value of the attribute
 * @return the value of the attribute
 */
fun <T> Attribute<T>.getOrSet(generator: () -> T): T {
    if (this.get() == null) {
        this.set(generator())
    }

    return this.get()
}