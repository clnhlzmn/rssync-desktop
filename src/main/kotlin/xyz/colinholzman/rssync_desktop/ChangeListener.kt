package xyz.colinholzman.rssync_desktop

class ChangeListener(
    private val updatePeriod: Long,
    private val initial: String?,
    private val getter: ()->String?,
    private val listener: (String?)->Unit) {

    class UpdaterThread(
        private val updatePeriod: Long,
        initial: String?,
        val getter: ()->String?,
        val listener: (String?)->Unit): Thread() {
        private var lastValue = initial
        var running = true
        override fun run() {
            while (running) {
                val newValue = getter()
                if (newValue != lastValue)
                    listener(newValue)
                lastValue = newValue
                sleep(updatePeriod)
            }
        }
    }

    private var thread: UpdaterThread? = null

    fun start() {
        if (thread == null) {
            thread = UpdaterThread(updatePeriod, initial, getter, listener)
            thread?.start()
        }
    }

    fun stop() {
        thread?.running = false
        thread?.join()
        thread = null
    }

}