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
import javax.swing.Action
import kotlin.system.exitProcess
import java.awt.event.MouseAdapter
import com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener
import java.awt.event.MouseEvent


class Main {

    companion object {

        var settings: SettingsDialog? = null

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
            //TODO: add about screen
            val exitItem = MenuItem("Exit")
            exitItem.addActionListener {
                exitProcess(0)
            }

            //Add components to pop-up menu
            popup.add(aboutItem)
            popup.add(exitItem)

            trayIcon.popupMenu = popup
            trayIcon.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (e.clickCount == 1 && e.button == MouseEvent.BUTTON1) {
                        if (settings != null) {
                            settings!!.isVisible = !settings!!.isVisible
                        }
                    }
                }
            })

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
                settings = SettingsDialog()
            }

            rs.start()

        }
    }
}

