package it.wetaxi.test.message.models

import it.wetaxi.test.message.network.objects.MessageObj

class Message : MessageObj {

    constructor(message: MessageObj) : super() {
        title = message.title
        text = message.text
        date = message.date
        time = message.time
        priority = message.priority
    }

    constructor(read: Boolean) : super() {
        this.read = read
    }

    var read: Boolean = false

}