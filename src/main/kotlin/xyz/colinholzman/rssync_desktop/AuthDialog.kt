package xyz.colinholzman.rssync_desktop

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.scene.web.WebEvent
import javafx.scene.web.WebView
import xyz.colinholzman.remotestorage_kotlin.*
import java.awt.Dimension
import java.net.URL
import javax.swing.*

class AuthDialog(
    user: String,
    onAuthorizationDenied: (String) -> Unit,
    onAuthorizationGranted: (JSONResourceDescriptor, String) -> Unit)
    : JFrame("Authorize") {

    init {
        this.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        //create layout and assign it to this
        val layout = SpringLayout()
        this.layout = layout

        val pane = this.contentPane
        pane.preferredSize = Dimension(400, 400)

        // You should execute this part on the Event Dispatch Thread
        // because it modifies a Swing component
        val jfxPanel = JFXPanel()
        jfxPanel.preferredSize = Dimension(400, 300)
        this.add(jfxPanel)
        jfxPanel.isVisible = false

        layout.putConstraint(SpringLayout.WEST, jfxPanel, 5, SpringLayout.WEST, pane)
        layout.putConstraint(SpringLayout.NORTH, jfxPanel, 5, SpringLayout.NORTH, pane)
        layout.putConstraint(SpringLayout.EAST, jfxPanel, -5, SpringLayout.EAST, pane)
        layout.putConstraint(SpringLayout.SOUTH, jfxPanel, -5, SpringLayout.SOUTH, pane)

        this.pack()

        Discovery.lookup(
            user,
            {
                println("Discovery: fail")
                this.isVisible = false
                dispose()
            },
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
                                        map.containsKey("access_token") -> onAuthorizationGranted(it, map["access_token"]!!)
                                        map.containsKey("error") -> onAuthorizationDenied(map["error"]!!)
                                        else -> onAuthorizationDenied("unknown")
                                    }
                                    this.isVisible = false
                                    dispose()
                                }
                            }
                        }
                    jfxPanel.isVisible = true
                }
            }
        )
    }

}