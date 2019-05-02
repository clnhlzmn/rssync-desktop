package xyz.colinholzman.rssync_desktop

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import xyz.colinholzman.remotestorage_kotlin.RemoteStorage

fun main(args: Array<String>) {

    //TODO: get user address and token from args
    val userAddress = ""
    val token = ""

    //TODO: get href from user address using Discovery
    val href = userAddress
    val rs = RemoteStorage(href, token)

    var lastRemote: String? = null
    var lastLocal: String? = null

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
    }

}

