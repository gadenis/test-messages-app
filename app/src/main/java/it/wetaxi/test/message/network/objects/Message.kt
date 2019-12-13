package it.wetaxi.test.message.network.objects

import com.google.gson.annotations.SerializedName

open class MessageObj {

    @SerializedName("title")
    var title: String? = null

    @SerializedName("text")
    var text: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("time")
    var time: String? = null

    //message priority 1-5
    @SerializedName("priority")
    var priority: String? = null
}