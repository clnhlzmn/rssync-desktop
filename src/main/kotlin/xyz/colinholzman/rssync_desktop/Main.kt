package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.*
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.AWTException
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.event.MouseEvent
import java.util.*


class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            val prefs = Preferences.get()
            if (!prefs.containsKey(Preferences.clientId)) {
                val newPrefs = prefs + mapOf(Preferences.clientId to UUID.randomUUID().toString())
                Preferences.set(newPrefs)
            }

            val settings = SettingsDialog()
            settings.isVisible = true

        }
    }
}

