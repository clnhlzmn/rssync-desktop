package xyz.colinholzman.rssync_desktop

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebView
import java.awt.Dimension

import javax.swing.*


class GUI : JFrame("hello") {
    init {
        this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        //create layout and assign it to this
        val layout = SpringLayout()
        this.layout = layout

        val pane = this.contentPane

        // You should execute this part on the Event Dispatch Thread
        // because it modifies a Swing component
        val jfxPanel = JFXPanel()
        jfxPanel.preferredSize = Dimension(400, 300)
        this.add(jfxPanel)
        jfxPanel.isVisible = false

        val userNameField = JTextField("user@domain", 15)
        this.add(userNameField)
        val connectButton = JButton("Connect")
        connectButton.addActionListener { jfxPanel.isVisible = !jfxPanel.isVisible }
        this.add(connectButton)

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

        this.pack()
        this.isVisible = true

    }
}