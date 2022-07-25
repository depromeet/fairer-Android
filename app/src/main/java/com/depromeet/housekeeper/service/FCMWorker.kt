package com.depromeet.housekeeper.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class FCMWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {

        CoroutineScope(Dispatchers.IO).launch {
            Repository.saveToken(PrefsManager.deviceToken)
                .runCatching {
                    collect {
                    }
                }
                .onFailure {
                    Timber.e("$it")
                }
        }

        return Result.success()
    }

}