package com.depromeet.housekeeper.service

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.network.remote.repository.Repository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MyFirebaseMessagingService: FirebaseMessagingService() {
    @Override
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New FCM device token : $token")
        PrefsManager.setDeviceToken(deviceToken = token)
        sendTokenToServer()
    }

    private fun sendTokenToServer() {
        val constraints = Constraints.Builder()
            /* 네트워크 연결상태에 대한 제약 조건 */
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

            /* 제약조건과 함께 작업을 생성하거나 */
        val workRequest = OneTimeWorkRequestBuilder<FCMWorker>()
            .setConstraints(constraints)
            .build()

        /* 작업을 생성하고 나중에 제약조건을 설정해 줄수 있다 */
        val workManager = WorkManager.getInstance(application)
        workManager.enqueue(workRequest)
    }

    @Override
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // TODO : 메세지 수신 후 처리
    }

    private fun showNotification(messageTitle: String, messageBody: String) {
        // TODO : 수신된 FCM 메세지를 포함하는 간단한 알림을 만들고 표시
    }
}