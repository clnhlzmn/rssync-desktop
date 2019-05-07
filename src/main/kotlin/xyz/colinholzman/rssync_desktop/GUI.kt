package xyz.colinholzman.rssync_desktop

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebView
import xyz.colinholzman.remotestorage_kotlin.Authorization
import xyz.colinholzman.remotestorage_kotlin.Discovery
import java.awt.Dimension

import javax.swing.*
import javafx.event.EventHandler
import javafx.scene.web.WebEngine
import javafx.scene.web.WebEvent
import xyz.colinholzman.remotestorage_kotlin.JSONResourceDescriptor
import xyz.colinholzman.remotestorage_kotlin.RemoteStorage
import java.net.URL


class GUI {

    companion object {

        fun authorize(
            onAuthorizationDenied: (String) -> Unit,
            onAuthorizationGranted: (JSONResourceDescriptor, String) -> Unit
        ) {
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
                        // Creation of scene and future interactions with JFXPanel
                        // should take place on the JavaFX Application Thread
                        Platform.runLater {
                            val webView = WebView()
                            jfxPanel.scene = Scene(webView)
                            webView.engine.load(Authorization.getAuthQuery(it))
                            val oldListener = webView.engine.onStatusChanged
                            //get access token from redirect url
                            webView.engine.onStatusChanged =
                                EventHandler<WebEvent<String>> { event ->
                                    if (event.source is WebEngine) {
                                        val we = event.source as WebEngine
                                        val location = we.location
                                        if (location.startsWith(Authorization.redirectUrl)) {
                                            val url = URL(location)
                                            val map = Authorization.getMap(url.ref)
                                            when {
                                                map.containsKey("access_token") -> {
                                                    onAuthorizationGranted(it, map["access_token"]!!)
                                                    webView.engine.onStatusChanged = oldListener
                                                }
                                                map.containsKey("error") -> onAuthorizationDenied(map["error"]!!)
                                                else -> onAuthorizationDenied("error")
                                            }
                                        }
                                    }
                                }
                            jfxPanel.isVisible = true
                        }
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

            frame.pack()
            frame.isVisible = true
        }

        fun connected(rs: RemoteStorage) {

        }

    }
}