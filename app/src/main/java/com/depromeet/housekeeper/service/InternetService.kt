package com.depromeet.housekeeper.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Binder
import android.os.IBinder
import timber.log.Timber

class InternetService : Service() {
    private var binder: IBinder = InternetBinder()
    private lateinit var networkCallback: CheckNetworkConnected

    inner class InternetBinder : Binder() {
        val service: InternetService
            get() = this@InternetService
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d(">>>>>>>> InternetService onCreate")
        networkCallback = CheckNetworkConnected(applicationContext)
        networkCallback.register()
    }

    override fun onBind(p0: Intent?): IBinder? {
        Timber.d(">>>>>>>> InternetService onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.d(">>>>>>>> InternetService onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d(">>>>>>>> InternetService onDestroy")
    }

    class CheckNetworkConnected(context: Context) : ConnectivityManager.NetworkCallback() {
        private val networkRequest: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        private val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        private var isNetworkConnected = false

        fun register() {
            connectivityManager.registerNetworkCallback(networkRequest, this)
            Timber.d(">>> CheckNetwork registered")
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Timber.d(">>> CheckNetwork onAvailable")
            isNetworkConnected = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Timber.d(">>> CheckNetwork onLost")
            isNetworkConnected = false
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Timber.d(">>> CheckNetwork onUnavailable")
            isNetworkConnected = false
        }
    }
}