package com.depromeet.housekeeper.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.Token
import com.depromeet.housekeeper.ui.HouseKeeperActivity
import com.depromeet.housekeeper.util.PrefsManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FairerFirebaseMessagingService : FirebaseMessagingService()
{
    @Inject
    lateinit var userRepository: UserRepository

    @Override
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag(TAG_FCM).d("New FCM device token : $token")
        PrefsManager.setDeviceToken(deviceToken = token)
        sendRegisterationToServer(token)
    }

    @Override
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.tag(TAG_FCM).d("Message: $message")
        message.notification?.let {
            showNotification(messageTitle = it.title, messageBody = it.body)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(messageTitle: String?, messageBody: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = (System.currentTimeMillis() / 7).toInt() // 고유 ID 지정

        createNotificationChannel(notificationManager)

        val intent = Intent(this, HouseKeeperActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntentFlag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_IMMUTABLE or FLAG_ONE_SHOT else FLAG_ONE_SHOT
        val pendingIntent =
            PendingIntent.getActivity(this, notificationID, intent, pendingIntentFlag)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_fairer)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(soundUri)  // 알림 소리
            .setAutoCancel(true)  // 알림 터치 시 자동으로 삭제
            .setContentIntent(pendingIntent)
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

    private fun sendRegisterationToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.saveToken(Token(token))
        }
    }

    companion object {
        private const val CHANNEL_NAME = "FairerNotification"
        private const val CHANNEL_DESCRIPTION = "Channel For Fairer Notification"
        private const val CHANNEL_ID = "fcm_default_channel"
        private const val TAG_FCM = "onFCM"
    }
}