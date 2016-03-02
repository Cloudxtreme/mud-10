package uk.co.grahamcox.mud.server.telnet.ui

/**
 * Helper methods for generating ANSI Escape Sequences
 */
object ANSI {
    /** The Escape byte */
    private val ESC = 27.toByte()

    /** The CSI prefix */
    private val CSI: List<Byte> = listOf(ESC, '['.toByte())

    /**
     * Move the cursor to the given co-ordinates on the screen. These co-ordinates are 0-based, unlike ANSI which are
     * 1-based
     * @param x The X-Ordinate to move to
     * @param y The Y-Ordinate to move to
     * @return the ANSI escape sequence to move to the given location. This is CSI n ; m H
     */
    fun moveTo(x: Int, y: Int): List<Byte> = CSI +
            (y + 1).toString().toByteArray().toList() +
            ';'.toByte() +
            (x + 1).toString().toByteArray().toList() +
            'H'.toByte()

    /**
     * Clear the entire screen
     * @return the ANSI escape sequence to clear the screen
     */
    fun clearScreen(): List<Byte> = CSI + "2J".toByteArray().toList()
}
