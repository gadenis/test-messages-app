package it.wetaxi.test.message.models

import it.wetaxi.test.message.network.objects.MessageObj

class Message(message: MessageObj) : MessageObj() {

    init {
        title = message.title
        text = message.text
        date = message.date
        time = message.time
        priority = message.priority
    }

    var read: Boolean = false

}