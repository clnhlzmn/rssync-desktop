package xyz.colinholzman.rssync_desktop

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebView
import xyz.colinholzman.remotestorage_kotlin.Discovery
import java.awt.Dimension

import javax.swing.*


class GUI {

    fun authorize(onAuthorizationDenied: (String)->Unit, onAuthorizationGranted: (String)->Unit) {
        val frame = JFrame("connect")
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        //create layout and assign it to this
        val layout = SpringLayout()
        frame.layout = layout

        val pane = frame.contentPane

        // You should execute this part on the Event Dispatch Thread
        // because it modifies a Swing component
        val jfxPanel = JFXPanel()
        jfxPanel.preferredSize = Dimension(400, 300)
        frame.add(jfxPanel)
        jfxPanel.isVisible = false

        val userNameField = JTextField("user@domain", 15)
        frame.add(userNameField)
        val connectButton = JButton("Connect")
        connectButton.addActionListener {
            Discovery.lookup(
                userNameField.text,
                { println("Discovery: fail") },
                {
                    println("Discovered: $it")

                }
            )
        }
        frame.add(connectButton)

        layout.putConstraint(SpringLayout.WEST, userNameField, 5, SpringLayout.WEST, pane)
        layout.putConstraint(SpringLayout.WEST, connectButton, 5, SpringLayout.EAST, userNameField)

        layout.putConstraint(SpringLayout.NORTH, userNameField, 5, SpringLayout.NORTH, pane)
        layout.putConstraint(SpringLayout.NORTH, connectButton, 5, SpringLayout.NORTH, pane)

        layout.putConstraint(SpringLayout.WEST, jfxPanel, 5, SpringLayout.WEST, pane)
        layout.putConstraint(SpringLayout.NORTH, jfxPanel, 5, SpringLayout.SOUTH, connectButton)
        layout.putConstraint(SpringLayout.EAST, pane, 5, SpringLayout.EAST, jfxPanel)
        layout.putConstraint(SpringLayout.SOUTH, pane, 5, SpringLayout.SOUTH, jfxPanel)

        // Creation of scene and future interactions with JFXPanel
        // should take place on the JavaFX Application Thread
        Platform.runLater {
            val webView = WebView()
            jfxPanel.scene = Scene(webView)
            webView.engine.load("http://www.stackoverflow.com/")
        }

        frame.pack()
        frame.isVisible = true
    }
}