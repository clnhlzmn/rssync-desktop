package xyz.colinholzman.rssync_desktop

import net.sf.image4j.codec.ico.ICODecoder
import java.awt.*
import javax.swing.SwingUtilities
import java.awt.AWTException
import java.awt.CheckboxMenuItem
import java.awt.SystemTray
import java.awt.TrayIcon
import java.io.File
import javax.imageio.ImageIO


class Main {

    companion object {

        fun addSystemTrayIcon() {
            if (!SystemTray.isSupported()) {
                println("SystemTray is not supported")
                return
            }
            val popup = PopupMenu()
            val icon = Toolkit.getDefaultToolkit().getImage(Main::class.java.getResource("/ic_sync_white_48dp.png"))
            val trayIcon = TrayIcon(icon, "tray icon")
            val tray = SystemTray.getSystemTray()

            // Create a pop-up menu components
            val aboutItem = MenuItem("About")
            val cb1 = CheckboxMenuItem("Set auto size")
            val cb2 = CheckboxMenuItem("Set tooltip")
            val displayMenu = Menu("Display")
            val errorItem = MenuItem("Error")
            val warningItem = MenuItem("Warning")
            val infoItem = MenuItem("Info")
            val noneItem = MenuItem("None")
            val exitItem = MenuItem("Exit")

            //Add components to pop-up menu
            popup.add(aboutItem)
            popup.addSeparator()
            popup.add(cb1)
            popup.add(cb2)
            popup.addSeparator()
            popup.add(displayMenu)
            displayMenu.add(errorItem)
            displayMenu.add(warningItem)
            displayMenu.add(infoItem)
            displayMenu.add(noneItem)
            popup.add(exitItem)

            trayIcon.popupMenu = popup

            try {
                tray.add(trayIcon)
            } catch (e: AWTException) {
                println("TrayIcon could not be added.")
            }

        }


        val rs = RSSync()

        @JvmStatic
        fun main(args: Array<String>) {

            SwingUtilities.invokeLater {
                addSystemTrayIcon()
                val settings = SettingsDialog()
                settings.isVisible = true
            }

            rs.start()

        }
    }
}

