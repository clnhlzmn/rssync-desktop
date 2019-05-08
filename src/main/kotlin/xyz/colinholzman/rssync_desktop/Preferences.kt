package xyz.colinholzman.rssync_desktop

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class Preferences {

    companion object {

        private val preferencesPath =
            System.getProperty("user.home") + File.separator + ".rssync" + File.separator + "preferences"

        fun get(): Map<String, String> {
            val file = File(preferencesPath)
            return if (file.exists()) {
                val content = file.readText()
                Gson().fromJson<Map<String, String>>(content, object : TypeToken<Map<String, String>>() {}.type)
            } else {
                hashMapOf()
            }
        }

        fun set(value: Map<String, String>) {
            val content = Gson().toJson(value)
            val file = File(preferencesPath)
            file.createNewFile()
            file.printWriter().use {
                it.print(content)
            }
        }

    }

}