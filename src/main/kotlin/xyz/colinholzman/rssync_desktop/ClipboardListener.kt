package xyz.colinholzman.rssync_desktop

import java.awt.Toolkit
import java.awt.datatransfer.*

//modified from https://stackoverflow.com/a/14226456/3447936
class ClipboardListener : Thread(), ClipboardOwner {

    var lastContent: String? = null
    var notify: ((String) -> Unit)? = null
    var cb: Clipboard = Toolkit.getDefaultToolkit().systemClipboard

    fun setContent(content: String?) {
        val t = StringSelection(content)
        cb.setContents(t, null)
        lastContent = content
    }

    fun getContent(): String? {
        val t = cb.getContents(null)
        return if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            t.getTransferData(DataFlavor.stringFlavor) as String
        } else {
            null
        }
    }

    override fun run() {
        val trans = cb.getContents(this)
        takeOwnership(trans)
    }

    override fun lostOwnership(c: Clipboard, t: Transferable) {

        try {
            sleep(250)  //waiting e.g for loading huge elements like word's etc.
        } catch (e: Exception) {
            println("Exception: $e")
        }

        val contents = cb.getContents(this)
        processClipboard(contents, c)

        takeOwnership(contents)
    }

    private fun takeOwnership(t: Transferable) {
        cb.setContents(t, this)
    }

    private fun processClipboard(t: Transferable?, c: Clipboard) { //your implementation
        try {
            if (t?.isDataFlavorSupported(DataFlavor.stringFlavor) == true) {
                val newContent = t.getTransferData(DataFlavor.stringFlavor) as String
                if (newContent != lastContent)
                    notify?.invoke(newContent)
                lastContent = newContent
            }
        } catch (e: Exception) {
            println(e)
        }
    }

}