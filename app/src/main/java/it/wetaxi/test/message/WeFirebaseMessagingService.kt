package it.wetaxi.test.message

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import it.wetaxi.test.message.models.Message

class WeFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "WeFirebaseMessaging"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.notification?.let { it ->
            Log.d(TAG, "${it.tag} ${it.title} ${it.body}")

            val message = Message(false)
            val maps = remoteMessage.data
            message.title = maps["title"]
            message.text = maps["text"]
            message.date = maps["date"]
            message.time = maps["time"]
            message.priority = maps["priority"]

            if (messageHandler != null){
                messageHandler?.onNewNotification(message)
            }else{


                Log.d(TAG, "Messages size:" + MainActivity.messages.size)
                Log.d(TAG, "Add new message : " + message.toString())
                MainActivity.messages.add(message)
                Log.d(TAG, "New size:" + MainActivity.messages.size)


                if (!messageNotificationChanneBuilt) {
                    createNotificationChannel()
                }

                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

                val builder = NotificationCompat.Builder(this, MESSAGE_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_unread_24px)
                    .setContentTitle(it.title)
                    .setContentText(it.body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                with(NotificationManagerCompat.from(this)) {
                    notify(MESSAGE_NOTIFICATION_ID, builder.build())
                }
            }

        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        WeFirebaseMessagingService.token = token
        Log.d(TAG, "Refreshed token: $token")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.message_notification_channel_name)
            val descriptionText = getString(R.string.message_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MESSAGE_NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            messageNotificationChanneBuilt = true
        }else{
            //no need to create channel below Android O
            messageNotificationChanneBuilt = true
        }
    }

    companion object{

        var messageHandler : MessageNotificationHandler? = null

        //notification IDs
        var MESSAGE_NOTIFICATION_ID = 1000

        //channels
        var messageNotificationChanneBuilt = false
        val MESSAGE_NOTIFICATION_CHANNEL_ID = "MESSAGE_NOTIFICATION_CHANNEL_ID"

        var token: String? = null
    }

    interface MessageNotificationHandler{
        fun onNewNotification(message : Message)
    }
}
