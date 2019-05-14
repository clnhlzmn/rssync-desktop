package xyz.colinholzman.rssync_desktop

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.lang.Exception

class MQTT(val server: String, val port: String, val user: String?, val password: String?, val notify: () -> Unit) {

    private val broker = "tcp://$server:$port"
    private val clientId = Preferences.get()[Preferences.clientId]
    private val client = MqttClient(broker, clientId, MemoryPersistence())

    fun connect() {
        if (!client.isConnected) {
            try {
                val connOpts = MqttConnectOptions()
                connOpts.isCleanSession = true
                connOpts.userName = user
                connOpts.password = password?.toCharArray()
                client.connect(connOpts)
                client.subscribe("rssync/#") { topic, message ->
                    if (!topic.endsWith(clientId!!)) {
                        notify()
                    }
                }
            } catch (e: MqttException) {
                println(e)
            }
        }
    }

    fun publish() {
        try {
            client.publish("rssync/$clientId", MqttMessage())
        } catch (e: MqttException) {
            println(e)
        }
    }

    fun disconnect() {
        try {
            client.disconnect()
        } catch (e: MqttException) {
            println(e)
        }
    }

}