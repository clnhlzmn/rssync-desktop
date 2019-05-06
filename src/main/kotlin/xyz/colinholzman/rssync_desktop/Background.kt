package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.RemoteStorage
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

class Background(val rs: RemoteStorage) {

    val cb = Toolkit.getDefaultToolkit().systemClipboard

    private fun getClipboardContents(): String? {
        val t = cb.getContents(null)
        return if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            t.getTransferData(DataFlavor.stringFlavor) as String
        } else {
            null
        }
    }

    private val localListener = ChangeListener(
        1000,
        getClipboardContents(),
        { getClipboardContents() },
        { s -> println("Local: $s") }
    )

    private fun getServerContents(): String? {
        return rs.getSync("/clipboard/txt")
    }

    private val remoteListener = ChangeListener(
        1000,
        getServerContents(),
        { getServerContents() },
        { s -> println("Server: $s") }
    )

    fun start() {
        localListener.start()
        remoteListener.start()
    }

    fun stop() {
        localListener.stop()
        remoteListener.stop()
    }
}