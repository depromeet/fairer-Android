package com.depromeet.housekeeper.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.depromeet.housekeeper.util.FILTER_INTERNET_CONNECTED
import com.depromeet.housekeeper.util.IS_INTERNET_CONNECTED
import timber.log.Timber

class InternetService : Service() {
    private var binder: IBinder? = InternetBinder()
    private var networkCallback: CheckNetworkConnected? = null

    inner class InternetBinder : Binder() {
        val service: InternetService
            get() = this@InternetService
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d(">>>>>>>> InternetService onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.d(">>>>>>>> InternetService onStartCommand")
        return START_REDELIVER_INTENT
    }

    override fun onBind(p0: Intent?): IBinder? {
        Timber.d(">>>>>>>> InternetService onBind")
        if (networkCallback == null) {
            networkCallback = CheckNetworkConnected(applicationContext)
            networkCallback!!.register()
        }
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        super.onUnbind(intent)
        Timber.d(">>>>>>>> InternetService onUnbind")
        binder = null
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binder = null
        networkCallback?.unregister()
        Timber.d(">>>>>>>> InternetService onDestroy")
    }

    class CheckNetworkConnected(val context: Context) : ConnectivityManager.NetworkCallback() {
        private val networkRequest: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        private val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        fun register() {
            connectivityManager.registerNetworkCallback(networkRequest, this)
            Timber.d(">>> CheckNetwork registered")
        }

        fun unregister() {
            connectivityManager.unregisterNetworkCallback(this)
            Timber.d(">>> CheckNetwork unregistered")
        }

        private fun sendInternetConnected(value: Boolean) {
            val intent = Intent(FILTER_INTERNET_CONNECTED).apply {
                this.action = ACTION_SEND
                this.putExtra(IS_INTERNET_CONNECTED, value)
            }
            context.sendBroadcast(intent)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Timber.d(">>> CheckNetwork onAvailable")
            sendInternetConnected(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Timber.d(">>> CheckNetwork onLost")
            sendInternetConnected(false)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Timber.d(">>> CheckNetwork onUnavailable")
            sendInternetConnected(false)
        }
    }
}