package com.depromeet.housekeeper.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.Token
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class FCMWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    override suspend fun doWork(): Result {
        Repository.saveToken(Token(PrefsManager.deviceToken))
            .runCatching {
                collect {
                    Timber.d("fcm 토큰 보내기 성공 + $this")
                }
            }
            .onFailure {
                return Result.retry()
            }
        return Result.success()
    }
}
