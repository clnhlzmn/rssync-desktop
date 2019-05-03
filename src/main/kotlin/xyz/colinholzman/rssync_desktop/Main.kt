package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.RemoteStorage

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebView

import javax.swing.*


class Hello public constructor() : JFrame("hello") {
    init {
        this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        //        this.add(new JLabel("Hello, world!"));

        // You should execute this part on the Event Dispatch Thread
        // because it modifies a Swing component
        val jfxPanel = JFXPanel()
        this.add(jfxPanel)

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

fun main(args: Array<String>) {

    //TODO: get user address and token from args
    val userAddress = ""
    val token = ""

    //TODO: get href from user address using Discovery
    val href = userAddress
    val rs = RemoteStorage(href, token)

    var lastRemote: String? = null
    var lastLocal: String? = null

    Hello()

    val cb = Toolkit.getDefaultToolkit().systemClipboard
    while (true) {
        try {
            //check for local changes
            val t = cb.getContents(null)
            if (t == null && lastLocal != null) {
                //TODO: delete remote
            } else if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                var text = t.getTransferData(DataFlavor.stringFlavor) as String
                text = text.toUpperCase();
                val ss = StringSelection(text);
                Toolkit.getDefaultToolkit().systemClipboard.setContents(ss, null);
            }

            //check for remote changes

        } catch (e: Exception) {
            print("Error: $e")
        }

        Thread.sleep(1000)
    }

}

