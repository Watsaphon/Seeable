package com.estazo.project.seeable.app


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d("FBMessagingService", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d("FBMessagingService", "sendRegistrationTokenToServer($token)")
    }

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
        Log.d("FBMessagingService", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FBMessagingService", "Message data payload: ${remoteMessage.data}")

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
            Log.d("FBMessagingService", "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        val title = remoteMessage.notification!!.title!!
        val body = remoteMessage.notification!!.body!!
        Log.d("FBMessagingService", "title : $title , body : $body ")
        sendNotification(title,body)
    }
    // [END receive_message]

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
        Log.d("FBMessagingService", "Short lived task is done.")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageTitle: String,messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.icon_delete_foreground)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
//    private lateinit var title: String
//    private lateinit var message: String
//    private lateinit var manager : NotificationManager
//    var CHANNEL_ID = "CHANNEL"


//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        FirebaseInstanceId.getInstance().token
//        Log.d("testNotification", "Refreshed token: $token")
//
//
//    }


//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
////         title = remoteMessage.data["title"]!!
////         message = remoteMessage.data["message"]!!
//        title = remoteMessage.notification!!.title!!
//        message = remoteMessage.notification!!.body!!
//         if (message == null){
//           message = Objects.requireNonNull(remoteMessage.notification!!.body)!!
////             this.bar = Objects.requireNonNull(bar, "bar must not be null");
//
//        }
//        manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        sendNotification()
//    }
//
//    private fun sendNotification() {
//        val intent = Intent(applicationContext,MainActivity::class.java)
//        intent.putExtra("tittle",title)
//        intent.putExtra("message",message)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(CHANNEL_ID,
//                "push notification", NotificationManager.IMPORTANCE_DEFAULT)
//
//
////            notificationManager.createNotificationChannel(channel)
//            manager.createNotificationChannel(channel)
//
//        }
//
//        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(title)
//            .setAutoCancel(true)
//            .setContentText(message)
//            .setSmallIcon(R.mipmap.icon_delete_foreground)
//
//        var pendingIntent = PendingIntent.getActivity(applicationContext,0,intent, PendingIntent.FLAG_ONE_SHOT)
//
//        builder.setContentIntent(pendingIntent)
//        manager.notify(0,builder.build())
//
//    }


}
