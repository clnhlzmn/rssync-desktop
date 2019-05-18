package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.RemoteStorage
import java.awt.Toolkit

class RSSync {

    private var rs = RemoteStorage("href", "token")

    private var clipboardListener = ClipboardListener()

    private var started = false

    private fun getServerContent(): String? {
        return rs.getSync("/clipboard/txt")
    }

    private fun setServerContent(value: String?) {
        if (value != null) {
            rs.putSync("/clipboard/txt", value)
        } else {
            rs.deleteSync("/clipboard/txt")
        }
    }

    fun start() {
        if (!started) {

            val prefs = Preferences.get()

            val href = prefs[Preferences.rsHref]
            val token = prefs[Preferences.rsToken]
            if (href != null && token != null) {
                rs = RemoteStorage(href, token)
            }

            clipboardListener.notify = {
                val content = clipboardListener.getContent()
                println("local changed: $content")
                setServerContent(content)
            }

            clipboardListener.start()

            started = true
        }
    }

    fun stop() {
        if (started) {
            clipboardListener.listening = false
            clipboardListener = ClipboardListener()
        }
    }
}