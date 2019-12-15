package it.wetaxi.test.message.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import it.wetaxi.test.message.network.objects.MessageObj

@Entity(tableName = "messages")
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

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "message_id")
    var messageId: Long = 0

    var read: Boolean = false

}