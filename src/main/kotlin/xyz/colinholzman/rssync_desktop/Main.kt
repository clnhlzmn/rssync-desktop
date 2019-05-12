package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.*
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.AWTException
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.event.MouseEvent


class Main {

    companion object {

        private fun start(href: String, token: String) {
            val rs = RemoteStorage(href, token)
            val bg = Background(rs)
            bg.start()

            //test mqtt
            val prefs = Preferences.get()
            val host = prefs["mqtt_server"]
            val port = prefs["mqtt_port"]
            val user = prefs["mqtt_user"]
            val pass = prefs["mqtt_password"]
            if (host != null && port != null && user != null && pass != null)
                MQTT(host, port, user, pass).test()
        }

        @JvmStatic
        fun main(args: Array<String>) {

            val settings = SettingsDialog()
            settings.isVisible = true

            val image = Toolkit.getDefaultToolkit().getImage("C:\\code\\rssync-desktop\\src\\main\\res\\favicon.ico")
            val trayIcon = TrayIcon(image)
            trayIcon.toolTip = "rssync"
            try {
                SystemTray.getSystemTray().add(trayIcon)
            } catch (e2: AWTException) {
                e2.printStackTrace()
            }

            trayIcon.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    settings.isVisible = true
                }
            })

            //try to get saved values
            val prefs = Preferences.get()
            val href = prefs["href"]
            val token = prefs["token"]

            if (href != null && token != null) {
                start(href, token)
            } else {
                //do authorization
                var auth: AuthDialog? = null
                auth = AuthDialog(
                    { println("Denied: $it") },
                    { jrd, newToken ->
                        println("Authorized: $token")
                        val newHref = Authorization.getHref(jrd)
                        val updatedPrefs = prefs + ("href" to newHref) + ("token" to newToken)
                        Preferences.set(updatedPrefs)
                        auth?.isVisible = false
                        start(newHref, newToken)
                    }
                )
                auth.isVisible = true
            }
        }
    }
}

