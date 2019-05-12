package xyz.colinholzman.rssync_desktop

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MQTT(val server: String, val port: String, val user: String?, val password: String?, val notify: () -> Unit) {

    private val broker = "tcp://$server:$port"
    private val clientId = Preferences.get()[Preferences.clientId]
    private val client = MqttClient(broker, clientId, MemoryPersistence())

    fun connect() {
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
    }

    fun publish() {
        client.publish("rssync/$clientId", MqttMessage())
    }

    fun disconnect() {
        client.disconnect()
    }

}