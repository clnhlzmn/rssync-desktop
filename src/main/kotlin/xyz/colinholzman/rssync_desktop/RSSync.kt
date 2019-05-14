package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.RemoteStorage
import java.awt.Toolkit

class RSSync {

    private var rs = RemoteStorage("href", "token")
    private var mqtt = MQTT("", "", "", "") {}

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

            val host = prefs[Preferences.mqttServer]
            val port = prefs[Preferences.mqttPort]
            val user = prefs[Preferences.mqttUser]
            val pass = prefs[Preferences.mqttPassword]
            if (host != null && port != null && user != null && pass != null) {
                mqtt.disconnect()
                mqtt = MQTT(host, port, user, pass) {
                    val content = getServerContent()
                    println("remote changed: $content")
                    clipboardListener.setContent(content)
                }
            }

            mqtt.connect()

            clipboardListener.notify = {
                val content = clipboardListener.getContent()
                println("local changed: $content")
                setServerContent(content)
                mqtt.publish()
            }

            clipboardListener.start()

            started = true
        }
    }

    fun stop() {
        if (started) {
            mqtt.disconnect()
            clipboardListener.listening = false
            clipboardListener = ClipboardListener()
        }
    }
}