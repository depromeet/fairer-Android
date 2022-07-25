package com.depromeet.housekeeper.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.Token
import com.depromeet.housekeeper.network.remote.repository.Repository

class FCMWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return if(PrefsManager.refreshToken.isNotBlank()) {
            val response = runCatching { Repository.saveToken(Token(PrefsManager.deviceToken)) }

            if(response.isSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        } else { // refresh token 없을 시 retry
            Result.retry()
        }
    }
}