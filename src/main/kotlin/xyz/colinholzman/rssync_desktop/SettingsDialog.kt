package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.Authorization
import java.awt.*
import javax.swing.*
import javax.swing.border.EtchedBorder
import javax.swing.border.TitledBorder
import javax.swing.JPanel
import javax.swing.ScrollPaneConstants
import javax.swing.JScrollPane
import javax.swing.JTextArea





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
                { println("Denied: $it") },
                { jrd, newToken ->
                    println("Authorized: $newToken")
                    val newHref = Authorization.getHref(jrd)
                    val currentPrefs = Preferences.get()
                    val updatedPrefs =
                        currentPrefs + (Preferences.rsHref to newHref) + (Preferences.rsToken to newToken)
                    Preferences.set(updatedPrefs)
                    rsTokenField.text = newToken
                }
            )
            auth.isVisible = true
        }
        rsPanel.add(button, gbc)

        //MQTT section
        val mqttPanel = JPanel()
        mqttPanel.layout = GridBagLayout()
        mqttPanel.border = TitledBorder(EtchedBorder(), "MQTT")
        gbc = GridBagConstraints()
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridy = 1
        this.add(mqttPanel, gbc)

        //mqtt server
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        mqttPanel.add(JLabel("server:"), gbc)

        gbc.insets = Insets(0, 0, 5, 5)
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        val mqttServerField = JTextField(prefs[Preferences.mqttServer] ?: "example.com")
        mqttServerField.document.addDocumentListener(DocumentChangeListener {
            Preferences.update(Preferences.mqttServer, mqttServerField.text)
        })
        mqttPanel.add(mqttServerField, gbc)

        //mqtt port
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridx = 0
        gbc.gridy = 1
        mqttPanel.add(JLabel("port:"), gbc)

        gbc.insets = Insets(0,0, 5, 5)
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        val mqttPortField = JTextField(prefs[Preferences.mqttPort] ?: "12345")
        mqttPortField.document.addDocumentListener(DocumentChangeListener {
            Preferences.update(Preferences.mqttPort, mqttPortField.text)
        })
        mqttPanel.add(mqttPortField, gbc)

        //mqtt user
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridx = 0
        gbc.gridy = 2
        mqttPanel.add(JLabel("user:"), gbc)

        gbc.insets = Insets(0,0, 5, 5)
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        val mqttUserField = JTextField(prefs[Preferences.mqttUser] ?: "user")
        mqttUserField.document.addDocumentListener(DocumentChangeListener {
            Preferences.update(Preferences.mqttUser, mqttUserField.text)
        })
        mqttPanel.add(mqttUserField, gbc)

        //mqtt password
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridx = 0
        gbc.gridy = 3
        mqttPanel.add(JLabel("password:"), gbc)

        gbc.insets = Insets(0,0, 5, 5)
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        val mqttPasswordField = JTextField(prefs[Preferences.mqttPassword] ?: "***")
        mqttPasswordField.document.addDocumentListener(DocumentChangeListener {
            Preferences.update(Preferences.mqttPassword, mqttPasswordField.text)
        })
        mqttPanel.add(mqttPasswordField, gbc)

        //log section
        val logPanel = JPanel()
        logPanel.layout = BorderLayout()
        logPanel.border = TitledBorder(EtchedBorder(), "Log")
        gbc = GridBagConstraints()
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridy = 2
        logPanel.preferredSize = Dimension(400, 400)
        this.add(logPanel, gbc)

        val logArea = JTextArea()
        logArea.isEditable = false
        val logScroll = JScrollPane()
        logScroll.add(logArea)
        logScroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        logPanel.add(logScroll, BorderLayout.CENTER)

        this.pack()
    }

}