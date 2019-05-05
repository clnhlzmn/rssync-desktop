package xyz.colinholzman.rssync_desktop

class ChangeListener(
    val updatePeriod: Long,
    val initial: String?,
    val getter: ()->String?,
    val listener: (String?)->Unit) {

    class UpdaterThread(
        val updatePeriod: Long,
        val initial: String?,
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
                Thread.sleep(updatePeriod)
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