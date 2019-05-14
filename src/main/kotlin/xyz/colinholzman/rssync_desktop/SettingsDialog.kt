package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.Authorization
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

class SettingsDialog: JFrame("rssync") {

    init {
        this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        this.layout = GridBagLayout()

        val rs = RSSync()

        val prefs = Preferences.get()

        val saveButton = JButton("save")

        //remoteStorage section
        var gbc = GridBagConstraints()
        gbc.insets = Insets(5, 5, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        this.add(JLabel("remoteStorage"), gbc)

        //user label
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridy = 1
        this.add(JLabel("user:"), gbc)

        //user field
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 0, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        gbc.gridy = 1
        val rsUserField = JTextField(prefs[Preferences.rsUser] ?: "user@example.com")
        rsUserField.document.addDocumentListener(DocumentChangeListener { saveButton.isEnabled = true })
        this.add(rsUserField, gbc)

        //token label
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridy = 2
        this.add(JLabel("token:"), gbc)

        //token field
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 0, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        gbc.gridy = 2
        val rsTokenField = JTextField(prefs[Preferences.rsToken] ?: "***")
        rsTokenField.isEditable = false
        this.add(rsTokenField, gbc)

        //authorize button
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 3
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
        this.add(button, gbc)

        //separator
        gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.gridy = 4
        gbc.weightx = 1.0
        gbc.insets = Insets(0, 5, 5, 5)
        this.add(JSeparator(), gbc)

        //MQTT section
        gbc = GridBagConstraints()
        gbc.gridy = 5
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        this.add(JLabel("MQTT"), gbc)

        //mqtt server
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridx = 0
        gbc.gridy = 6
        this.add(JLabel("server:"), gbc)

        gbc.insets = Insets(0, 0, 5, 5)
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        val mqttServerField = JTextField(prefs[Preferences.mqttServer] ?: "example.com")
        mqttServerField.document.addDocumentListener(DocumentChangeListener { saveButton.isEnabled = true })
        this.add(mqttServerField, gbc)

        //mqtt port
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridx = 0
        gbc.gridy = 7
        this.add(JLabel("port:"), gbc)

        gbc.insets = Insets(0,0, 5, 5)
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        val mqttPortField = JTextField(prefs[Preferences.mqttPort] ?: "12345")
        mqttPortField.document.addDocumentListener(DocumentChangeListener { saveButton.isEnabled = true })
        this.add(mqttPortField, gbc)

        //mqtt user
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridx = 0
        gbc.gridy = 8
        this.add(JLabel("user:"), gbc)

        gbc.insets = Insets(0,0, 5, 5)
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        val mqttUserField = JTextField(prefs[Preferences.mqttUser] ?: "user")
        mqttUserField.document.addDocumentListener(DocumentChangeListener { saveButton.isEnabled = true })
        this.add(mqttUserField, gbc)

        //mqtt password
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridx = 0
        gbc.gridy = 9
        this.add(JLabel("password:"), gbc)

        gbc.insets = Insets(0,0, 5, 5)
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        val mqttPasswordField = JTextField(prefs[Preferences.mqttPassword] ?: "***")
        mqttPasswordField.document.addDocumentListener(DocumentChangeListener { saveButton.isEnabled = true })
        this.add(mqttPasswordField, gbc)

        //separator
        gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.gridy = 10
        gbc.weightx = 1.0
        gbc.insets = Insets(0, 5, 5, 5)
        this.add(JSeparator(), gbc)

        //authorize button
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 11
        val startButton = JButton("start")
        startButton.addActionListener{
            println("start button")
            rs.start()
        }
        this.add(startButton, gbc)

        //save button
        gbc = GridBagConstraints()
        gbc.insets = Insets(0, 5, 5, 5)
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 12
        saveButton.addActionListener{
            val currentPrefs = Preferences.get()
            val updatedPrefs = currentPrefs + mapOf(
                Preferences.rsUser to rsUserField.text,
                Preferences.mqttServer to mqttServerField.text,
                Preferences.mqttPort to mqttPortField.text,
                Preferences.mqttUser to mqttUserField.text,
                Preferences.mqttPassword to mqttPasswordField.text
            )
            Preferences.set(updatedPrefs)
            saveButton.isEnabled = false
        }
        saveButton.isEnabled = false
        this.add(saveButton, gbc)

        this.pack()
    }

}