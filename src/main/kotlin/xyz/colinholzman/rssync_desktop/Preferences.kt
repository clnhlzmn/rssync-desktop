package xyz.colinholzman.rssync_desktop

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class Preferences {

    companion object {

        val rsUser = "rs_user"
        val rsHref = "rs_href"
        val rsToken = "rs_token"

        private val preferencesDir =
            System.getProperty("user.home") + File.separator + ".rssync"

        private val preferencesFile =
            preferencesDir + File.separator + "preferences"

        fun get(): Map<String, String> {
            val file = File(preferencesFile)
            return if (file.exists()) {
                val content = file.readText()
                Gson().fromJson<Map<String, String>>(content, object : TypeToken<Map<String, String>>() {}.type)
            } else {
                hashMapOf()
            }
        }

        fun set(value: Map<String, String>) {
            val content = Gson().toJson(value)
            val dir = File(preferencesDir)
            dir.mkdirs()
            val file = File(preferencesFile)
            file.createNewFile()
            file.printWriter().use {
                it.print(content)
            }
        }

        fun update(key: String, value: String) {
            val current = get()
            val new = current + mapOf(key to value)
            set(new)
        }

    }

}