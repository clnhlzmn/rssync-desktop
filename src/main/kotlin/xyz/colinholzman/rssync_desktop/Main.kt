package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.SwingUtilities


class Main {

    companion object {

        val rs = RSSync()

        @JvmStatic
        fun main(args: Array<String>) {

            val prefs = Preferences.get()
            if (!prefs.containsKey(Preferences.clientId)) {
                val newPrefs = prefs + mapOf(Preferences.clientId to UUID.randomUUID().toString())
                Preferences.set(newPrefs)
            }

            SwingUtilities.invokeLater {
                val settings = SettingsDialog()
                settings.isVisible = true
            }

            rs.start()

        }
    }
}

