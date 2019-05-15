package xyz.colinholzman.rssync_desktop

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.lang.Exception

class MQTT(var server: String, var port: String, var user: String?, var password: String?, val notify: () -> Unit) {

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
                Log.println("[MQTT]: connected to $broker")
                client.subscribe("rssync/#") { topic, message ->
                    if (!topic.endsWith(clientId!!)) {
                        notify()
                    }
                }
            } catch (e: MqttException) {
                Log.println("[MQTT]: $e")
            }
        }
    }

    fun publish() {
        try {
            client.publish("rssync/$clientId", MqttMessage())
        } catch (e: MqttException) {
            Log.println("[MQTT]: $e")
        }
    }

    fun disconnect() {
        if (client.isConnected) {
            try {
                client.disconnect()
            } catch (e: MqttException) {
                Log.println("[MQTT]: $e")
            }
        }
    }

}