package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.RemoteStorage
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

class RSSync {

    private val cb = Toolkit.getDefaultToolkit().systemClipboard

    private var rs = RemoteStorage("href", "token")
    private var mqtt = MQTT("", "", "", "") {}

    private fun getClipboardContent(): String? {
        val t = cb.getContents(null)
        return if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            t.getTransferData(DataFlavor.stringFlavor) as String
        } else {
            null
        }
    }

    private fun setClipboardContent(value: String?) {
        val t = StringSelection(value)
        cb.setContents(t, null)
    }

    private var localListener: ChangeListener? = null

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

        val prefs = Preferences.get()

        val href = prefs[Preferences.rsHref]
        val token = prefs[Preferences.rsToken]
        if (href != null && token != null)
            rs = RemoteStorage(href, token)

        //test mqtt
        val host = prefs[Preferences.mqttServer]
        val port = prefs[Preferences.mqttPort]
        val user = prefs[Preferences.mqttUser]
        val pass = prefs[Preferences.mqttPassword]
        if (host != null && port != null && user != null && pass != null) {
            mqtt = MQTT(host, port, user, pass) {
                val content = getServerContent()
                println("remote changed: $content")
                setClipboardContent(content)
                //TODO: prevent local listener from noticing this change and publishing again
            }
        }

        mqtt.connect()

        localListener = ChangeListener(
            500,
            getClipboardContent(),
            { getClipboardContent() },
            {
                val content = getClipboardContent()
                println("local changed: $content")
                setServerContent(content)
                mqtt.publish()
            }
        )
        localListener?.start()

    }

    fun stop() {

    }
}