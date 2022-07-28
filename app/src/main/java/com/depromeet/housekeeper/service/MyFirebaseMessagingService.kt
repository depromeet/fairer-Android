package com.depromeet.housekeeper.service

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.Token
import com.depromeet.housekeeper.network.remote.repository.Repository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MyFirebaseMessagingService: FirebaseMessagingService() {
    @Override
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New FCM device token : $token")
        PrefsManager.setDeviceToken(deviceToken = token)
    }

    @Override
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // TODO : 메세지 수신 후 처리
    }

    private fun showNotification(messageTitle: String, messageBody: String) {
        // TODO : 수신된 FCM 메세지를 포함하는 간단한 알림을 만들고 표시
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

}