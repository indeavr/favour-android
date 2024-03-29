package com.example.allfavour.services.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.allfavour.MainActivity
import com.example.allfavour.R
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.example.allfavour.utility.HandleNotifications
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessaging
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.IOException


class FavourFCM : FirebaseMessagingService() {
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification("${it.title} ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]


    private fun sendRegistrationToServer(token: String) {
        val userId = AuthenticationProvider.getFirebaseUserId(this) ?: return

        val firebaseDB = FirebaseDatabase.getInstance().reference

        val childUpdates = HashMap<String, Any>()
        childUpdates["users/$userId/fcmTokens/$token"] = true
        childUpdates["fcmTokens/$token"] = true

        firebaseDB.updateChildren(childUpdates)
    }


    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
//        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
//        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun getIntent(): PendingIntent {
        val bundle = Bundle().apply {
            putString("Title", "The Witcher")
            putString("Year", "2019")
            putString("started_from", "notification")
        }

        return NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_navigation)
            .setDestination(R.id.provider_profile_dest)
            .setArguments(bundle)
            .createPendingIntent()
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String) {
        val pendingIntent = getIntent()

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle("Witcher")
            .setContentText(messageBody)
            .setStyle(NotificationCompat.BigTextStyle())
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_message)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    fun showNotification(msg: String) {

    }

    companion object {
        private const val TAG = "FavourFCM"

        // isAutoInitEnabled --> disabled by default from manifest

        fun enable(userId: String) {
            FirebaseMessaging.getInstance().isAutoInitEnabled = true

            // token will be send to server in onNewToken()
        }

        fun disable(userId: String) {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                deleteTokenFromFirebase(userId, it.token)

                FirebaseMessaging.getInstance().isAutoInitEnabled = false

                Thread {
                    try {
                        // Remove InstanceID initiate to unsubscribe all topic
                        // TODO: May be a better way to use FirebaseMessaging.getInstance().unsubscribeFromTopic()
                        FirebaseInstanceId.getInstance().deleteInstanceId()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }.start()
            }
        }

        private fun deleteTokenFromFirebase(userId: String, token: String) {
            val firebaseDB = FirebaseDatabase.getInstance().reference

            val childUpdates = HashMap<String, Any?>()
            childUpdates["users/$userId/fcmTokens/$token"] = null
            childUpdates["fcmTokens/$token"] = null

            firebaseDB.updateChildren(childUpdates)
        }
    }

}