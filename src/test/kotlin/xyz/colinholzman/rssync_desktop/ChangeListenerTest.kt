package xyz.colinholzman.rssync_desktop

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ChangeListenerTest {
    @Test
    fun test() {
        var i = 0
        val cl = ChangeListener(
            1000,
            "",
            { i.toString() },
            {
                assertEquals(it, i.toString())
                i++
            }
        )
        cl.start()
        Thread.sleep(10000)
    }
}
