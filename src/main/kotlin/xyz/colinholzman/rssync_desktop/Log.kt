package xyz.colinholzman.rssync_desktop

class Log {
    companion object {
        val listeners = ArrayList<(String)->Unit>()
        val log = ArrayList<String>()
        fun println(value: String) {
            log.add(value)
            listeners.forEach{ it(value) }
        }
    }
}