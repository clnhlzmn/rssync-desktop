package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.Authorization
import xyz.colinholzman.remotestorage_kotlin.RemoteStorage
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            GUI.authorize(
                { println("Denied: $it") },
                { jrd, token ->
                    println("Authorized: $token")
                    val href = Authorization.getHref(jrd)
                    val prefs = hashMapOf("href" to href, "token" to token )
                    Preferences.set(prefs)
                    val rs = RemoteStorage(href, token)
                    val bg = Background(rs)
                    bg.start()
                }
            )

//            Background()

        }
    }
}

