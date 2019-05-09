package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.*
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.AWTException
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.Toolkit.getDefaultToolkit
import java.awt.event.MouseEvent


class Main {

    companion object {

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
                val rs = RemoteStorage(href, token)
                val bg = Background(rs)
                bg.start()
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
                        val rs = RemoteStorage(newHref, newToken)
                        val bg = Background(rs)
                        bg.start()
                    }
                )
                auth.isVisible = true
            }
        }
    }
}

