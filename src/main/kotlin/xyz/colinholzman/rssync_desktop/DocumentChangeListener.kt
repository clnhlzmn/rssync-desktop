package xyz.colinholzman.rssync_desktop

import javax.swing.event.ChangeEvent
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

//adapted from
class DocumentChangeListener(private val listener: (e:ChangeEvent?) -> Unit): DocumentListener {
    override fun changedUpdate(e: DocumentEvent?) {
        listener.invoke(ChangeEvent(e?.document))
    }

    override fun insertUpdate(e: DocumentEvent?) {
        listener.invoke(ChangeEvent(e?.document))
    }

    override fun removeUpdate(e: DocumentEvent?) {
        listener.invoke(ChangeEvent(e?.document))
    }

}