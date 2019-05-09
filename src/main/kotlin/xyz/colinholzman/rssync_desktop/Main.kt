package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.*

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            //try to get saved values
            val prefs = Preferences.get()
            val href = prefs["href"]
            val token = prefs["token"]

            if (href != null && token != null) {
                val rs = RemoteStorage(href, token)
                val bg = Background(rs)
                bg.start()
            } else {
                //do authorization
                var auth: AuthDialog? = null
                auth = AuthDialog(
                    { println("Denied: $it") },
                    { jrd, newToken ->
                        println("Authorized: $token")
                        val newHref = Authorization.getHref(jrd)
                        val updatedPrefs = prefs + ("href" to newHref) + ("token" to newToken)
                        Preferences.set(updatedPrefs)
                        auth?.isVisible = false
                        val rs = RemoteStorage(newHref, newToken)
                        val bg = Background(rs)
                        bg.start()
                    }
                )
                auth.authorize()
            }
        }
    }
}

