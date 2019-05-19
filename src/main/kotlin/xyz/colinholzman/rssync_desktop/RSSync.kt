package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.RemoteStorage
import java.util.*

class RSSync {

    private var rs = RemoteStorage("href", "token")

    private var clipboardListener = ClipboardListener()

    private var remoteListener: ChangeListener? = null

    private var started = false

    private fun setServerContent(value: String?) {
        if (value!= null) {
            rs.put(
                path = "/clipboard/txt",
                value = value,
                onFailure = {},
                onSuccess = {}
            )
        } else {
            rs.delete(
                path = "/clipboard/txt",
                onFailure = {},
                onSuccess = {}
            )
        }
    }

    private fun initAndStartRemoteListener() {
        val prefs = Preferences.get()
        val href = prefs[Preferences.rsHref]
        val token = prefs[Preferences.rsToken]
        if (href != null && token != null) {
            //create instance with saved values
            rs = RemoteStorage(href, token)
            //attempt to get initial value
            rs.get(
                path = "/clipboard/txt",
                onFailure = {
                    println("unable to connect to server, retrying")
                    Timer().schedule(
                        object : TimerTask() {
                            override fun run() { initAndStartRemoteListener() }
                        },
                        10000
                    )
                },
                onSuccess = { initialValue ->
                    //got initial value, use to set up listener
                    remoteListener = ChangeListener(
                        updatePeriod = 1000,
                        current = initialValue,
                        getter = { success: (String?) -> Unit, fail: () -> Unit ->
                            rs.get(
                                path = "/clipboard/txt",
                                onFailure = { fail() },
                                onSuccess = { success(it) }
                            )
                        },
                        listener = {
                            println("remote changed: $it")
                            clipboardListener.setContent(it)
                        }
                    )
                    remoteListener!!.start()
                }
            )
        }
    }

    fun start() {
        if (!started) {

            initAndStartRemoteListener()

            clipboardListener.notify = {
                println("local changed: $it")
                setServerContent(it)
                //set current on remotelistener so it doesn't notify of new remote value
                remoteListener?.current = it
            }

            clipboardListener.start()

            started = true
        }
    }

    fun stop() {
        if (started) {
            clipboardListener.listening = false
            clipboardListener = ClipboardListener()
            remoteListener?.stop()
        }
    }
}