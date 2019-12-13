package it.wetaxi.test.message.network.objects

import com.google.gson.annotations.SerializedName

class MessageResponse {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: MessageObj? = null

}