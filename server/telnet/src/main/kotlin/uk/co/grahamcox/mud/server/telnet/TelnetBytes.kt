package uk.co.grahamcox.mud.server.telnet

/**
 * Collection of the possible bytes with special meaning to Telnet
 */
object TelnetBytes {
    /** SE                  240    End of subnegotiation parameters. */
    val SE = 240.toByte()
    /** NOP                 241    No operation. */
    val NOP = 241.toByte()
    /** Data Mark           242    The data stream portion of a Synch. This should always be accompanied by a TCP Urgent notification. */
    val DataMark = 242.toByte()
    /** Break               243    NVT character BRK. */
    val Break = 243.toByte()
    /** Interrupt Process   244    The function IP. */
    val InterruptProcess = 244.toByte()
    /** Abort output        245    The function AO. */
    val AbortOutput = 245.toByte()
    /** Are You There       246    The function AYT. */
    val AreYouThere = 246.toByte()
    /** Erase character     247    The function EC. */
    val EraseCharacter = 247.toByte()
    /** Erase Line          248    The function EL. */
    val EraseLine = 248.toByte()
    /** Go ahead            249    The GA signal. */
    val GoAhead = 249.toByte()
    /** SB                  250    Indicates that what follows is subnegotiation of the indicated option. */
    val SB = 250.toByte()
    /** WILL (option code)  251    Indicates the desire to begin performing, or confirmation that you are now performing, the indicated option. */
    val WILL = 251.toByte()
    /** WON'T (option code) 252    Indicates the refusal to perform, or continue performing, the indicated option. */
    val WONT = 252.toByte()
    /** DO (option code)    253    Indicates the request that the other party perform, or confirmation that you are expecting the other party to perform, the indicated option. */
    val DO = 253.toByte()
    /** DON'T (option code) 254    Indicates the demand that the other party stop performing, or confirmation that you are no longer expecting the other party to perform, the indicated option. */
    val DONT = 254.toByte()
    /** IAC                 255    Data Byte 255. */
    val IAC = 255.toByte()
}
