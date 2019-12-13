package it.wetaxi.test.message.network.services

import android.os.Build
import it.wetaxi.test.message.BuildConfig
import it.wetaxi.test.message.network.objects.MessageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Messages {

    @GET("messages/useless")
    fun getMessages() : Call<MessageResponse>


//    @GET("/push/messages/useless")
//    fun pushMessage(@Query("token") token: String)

}