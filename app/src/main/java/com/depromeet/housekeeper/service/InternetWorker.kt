package com.depromeet.housekeeper.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class InternetWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        return Result.success()
    }
}