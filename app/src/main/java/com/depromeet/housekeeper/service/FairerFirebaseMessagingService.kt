package com.depromeet.housekeeper.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.depromeet.housekeeper.MainActivity
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.local.PrefsManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class FairerFirebaseMessagingService: FirebaseMessagingService() {

    @Override
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag(TAG_FCM).d("New FCM device token : $token")
        PrefsManager.setDeviceToken(deviceToken = token)
    }

    @Override
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("From: ${message.from}")
        if (message.data.isNotEmpty()) {
            // payload : 전송된 데이터
            Timber.tag(TAG_FCM).d("Message data payload: ${message.data}")

            val title = message.data["title"]
            val body = message.data["message"]

            showNotification(messageTitle = title, messageBody = body)
        }

        message.notification?.let {
            Timber.tag(TAG_FCM).d("Message Notification Body: ${it.body}")
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(messageTitle: String?, messageBody: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt() // 고유 id를 지정

        createNotificationChannel(notificationManager)

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, notificationID, intent, FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_fairer)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)  // 알림 터치 시 자동으로 삭제
            .build()

        notificationManager.notify(notificationID, notificationBuilder)
    }

    //Oreo(26) 이상 버전에는 channel 필요, 현재 MIN_SDK = 28
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH).apply {
            description = CHANNEL_DESCRIPTION
            enableLights(true)
            lightColor = getColor(R.color.highlight)
        }
        notificationManager.createNotificationChannel(channel)
    }

    // TODO : WorkerManager 이용해서 FCM Managing
    private fun sendFCMTokenToServer(context: Context) {
        val constraints = Constraints.Builder()
            /* 네트워크 연결상태 & 배터리 부족에 대한 제약 조건 */
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        // 10, 20, 40초 늘려가며 retry
        val workRequest = OneTimeWorkRequestBuilder<FCMWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(workRequest)
    }

    companion object {
        private const val CHANNEL_NAME = "FairerNotification"
        private const val CHANNEL_DESCRIPTION = "Channel For Fairer Notification"
        private const val CHANNEL_ID = "fcm_default_channel"
        private const val TAG_FCM = "onFCM"
    }
}