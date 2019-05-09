package xyz.colinholzman.rssync_desktop

import javafx.embed.swing.JFXPanel
import java.awt.Dimension
import javax.swing.*

class SettingsDialog: JFrame("rssync") {

    init {
        this.defaultCloseOperation = WindowConstants.HIDE_ON_CLOSE

        //create layout and assign it to this
        val layout = SpringLayout()
        this.layout = layout

        val pane = this.contentPane

        val prefs = Preferences.get()
        val href = prefs["href"]

        var connectedToField: JTextField? = null
        var connectButton: JButton? = null
        if (href == null) {
            val text = "disconnected"
            connectedToField = JTextField(text, text.length)
            connectButton = JButton("Connect")
        } else {
            connectedToField = JTextField(href, href.length)
            connectButton = JButton("Disconnect")
        }
        this.add(connectedToField)
        this.add(connectButton)

        layout.putConstraint(SpringLayout.WEST, connectedToField, 5, SpringLayout.WEST, pane)
        layout.putConstraint(SpringLayout.WEST, connectButton, 5, SpringLayout.EAST, connectedToField)

        layout.putConstraint(SpringLayout.NORTH, connectedToField, 5, SpringLayout.NORTH, pane)
        layout.putConstraint(SpringLayout.NORTH, connectButton, 5, SpringLayout.NORTH, pane)

        layout.putConstraint(SpringLayout.EAST, pane, 5, SpringLayout.EAST, connectButton)
        layout.putConstraint(SpringLayout.SOUTH, pane, 5, SpringLayout.SOUTH, connectButton)

        this.pack()
    }

}