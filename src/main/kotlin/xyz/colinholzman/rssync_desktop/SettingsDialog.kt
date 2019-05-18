package xyz.colinholzman.rssync_desktop

import javafx.event.EventDispatcher
import xyz.colinholzman.remotestorage_kotlin.Authorization
import java.awt.*
import javax.swing.*
import javax.swing.border.EtchedBorder
import javax.swing.border.TitledBorder
import javax.swing.JPanel
import javax.swing.ScrollPaneConstants
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.SwingUtilities
import javax.swing.border.Border


class SettingsDialog: JFrame("rssync") {

    init {

        this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        this.layout = GridBagLayout()

        //preferences for loading/saving fields
        val prefs = Preferences.get()

        //gbc for constraining layouts
        var gbc = GridBagConstraints()

        //remoteStorage section
        val rsPanel = JPanel()
        rsPanel.layout = GridBagLayout()
        rsPanel.border = TitledBorder(EtchedBorder(), "remoteStorage")
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        this.add(rsPanel, gbc)

        //user label
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        rsPanel.add(JLabel("user:"), gbc)

        //user field
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 0, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        val rsUserField = JTextField(prefs[Preferences.rsUser] ?: "user@example.com")
        rsUserField.document.addDocumentListener(DocumentChangeListener {
            Preferences.update(Preferences.rsUser, rsUserField.text)
        })
        rsPanel.add(rsUserField, gbc)

        //token label
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridy = 1
        rsPanel.add(JLabel("token:"), gbc)

        //token field
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 0, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        gbc.gridy = 1
        val rsTokenField = JTextField(prefs[Preferences.rsToken] ?: "***")
        rsTokenField.isEditable = false
        rsPanel.add(rsTokenField, gbc)

        //authorize button
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 2
        val button = JButton("authorize")
        button.addActionListener{
            println("authorize button")
            var auth: AuthDialog? = null
            auth = AuthDialog(
                rsUserField.text,
                {
                    println("Denied: $it")
                    auth?.isVisible = false
                },
                { jrd, newToken ->
                    println("Authorized: $newToken")
                    val newHref = Authorization.getHref(jrd)
                    val currentPrefs = Preferences.get()
                    val updatedPrefs =
                        currentPrefs + (Preferences.rsHref to newHref) + (Preferences.rsToken to newToken)
                    Preferences.set(updatedPrefs)
                    rsTokenField.text = newToken
                    auth?.isVisible = false
                }
            )
            auth.isVisible = true
        }
        rsPanel.add(button, gbc)

        this.pack()
    }

}