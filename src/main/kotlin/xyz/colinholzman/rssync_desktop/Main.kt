package xyz.colinholzman.rssync_desktop

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import xyz.colinholzman.remotestorage_kotlin.RemoteStorage

fun main(args: Array<String>) {

    val rs = RemoteStorage("", "")

    val task = Runnable { print("task") }
    val executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);

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

    while (!executorService.isShutdown) {
        continue
    }
}

