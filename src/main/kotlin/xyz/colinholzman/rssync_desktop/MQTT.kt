package xyz.colinholzman.rssync_desktop

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MQTT(val server: String, val port: String, val user: String?, val password: String?) {
    fun test() {
        val topic = "rssync"
        val content = "Message from rssync"
        val qos = 0
        val broker = "tcp://$server:$port"
        val clientId = "rssync"

        try {
            val client = MqttClient(broker, clientId, MemoryPersistence())

            val connOpts = MqttConnectOptions()
            connOpts.isCleanSession = true
            connOpts.userName = user
            connOpts.password = password?.toCharArray()

            println("Connecting to broker: $broker")
            client.connect(connOpts)
            println("Connected")

            println("Subscribing")
            //this sub won't use callback below because we give one here
            client.subscribe(topic) { topic, message ->
                println("Received: $message")
            }
            //this sub overrides the one above and uses callback below
            client.subscribe(topic)

            val callback = object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    println("messageArrived: $message")
                }

                override fun connectionLost(cause: Throwable?) {
                    println("connectionLost: $cause")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    println("deliveryComplete: $token")
                }
            }
            client.setCallback(callback)

            println("Publishing message: $content")
            val message = MqttMessage(content.toByteArray())
            message.qos = qos
            client.publish(topic, message)
            println("Message published")

//            sampleClient.disconnect()
//            println("Disconnected")
        } catch (me: MqttException) {
            println("reason " + me.reasonCode)
            println("msg " + me.message)
            println("loc " + me.localizedMessage)
            println("cause " + me.cause)
            println("excep $me")
            me.printStackTrace()
        }

    }
}