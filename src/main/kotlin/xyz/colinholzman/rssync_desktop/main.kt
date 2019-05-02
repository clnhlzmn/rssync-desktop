package xyz.colinholzman.rssync_desktop

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

fun main() {
    val t = Toolkit.getDefaultToolkit().systemClipboard.getContents(null)
    try {
        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            var text = t.getTransferData(DataFlavor.stringFlavor) as String
            text = text.toUpperCase();
            val ss = StringSelection(text);
            Toolkit.getDefaultToolkit().systemClipboard.setContents(ss, null);
        }
    } catch (e: Exception) {
        print("Error: $e")
    }
}

