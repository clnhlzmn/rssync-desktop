package xyz.colinholzman.rssync_desktop

import java.io.File



class Preferences {
    fun getUserDataDirectory(): String {
        return System.getProperty("user.home") + File.separator + ".rssync" + File.separator
    }
}