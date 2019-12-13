package it.wetaxi.test.message.network

import android.content.Context
import it.wetaxi.test.message.BuildConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient{

    private const val connectionTimeOut = 10
    private const val readTimeOut = 5
    private const val writeTimeOut = 5

    fun buildRetrofitClient(context: Context?): Retrofit {

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectionTimeOut.toLong(), TimeUnit.SECONDS)
            .readTimeout(readTimeOut.toLong(), TimeUnit.SECONDS)
            .writeTimeout(writeTimeOut.toLong(), TimeUnit.SECONDS)

        val client = okHttpClientBuilder.build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.DOMAIN + BuildConfig.API_VERSION + BuildConfig.ENVIRONMENT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

}