package xyz.colinholzman.rssync_desktop

import xyz.colinholzman.remotestorage_kotlin.RemoteStorage
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            //TODO: get user address and token from args
            val userAddress = ""
            val token = ""

            //TODO: get href from user address using Discovery
            val href = userAddress
            val rs = RemoteStorage(href, token)

            GUI()

            Background()

        }
    }
}

