package it.wetaxi.test.message

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WeFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "WeFirebaseMessaging"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.notification?.let { it ->
            Log.d(TAG, "${it.tag} ${it.title} ${it.body}")
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        WeFirebaseMessagingService.token = token
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object{
        var token: String? = null
    }
}
