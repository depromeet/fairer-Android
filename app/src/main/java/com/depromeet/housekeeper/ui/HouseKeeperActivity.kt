package com.depromeet.housekeeper.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ActivityHousekeeperBinding
import com.depromeet.housekeeper.service.InternetService
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HouseKeeperActivity : AppCompatActivity() {

    private val viewModel: HouseKeeperViewModel by viewModels()
    lateinit var binding: ActivityHousekeeperBinding
    private val internetServiceConnection = object: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val internetService = service as InternetService.InternetBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoading.value }
        }
        super.onCreate(savedInstanceState)
        applicationContext.bindService(Intent(this, InternetService::class.java), internetServiceConnection, Context.BIND_AUTO_CREATE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_housekeeper)
        binding.lifecycleOwner = this
        getDynamicLink()
    }

    private fun getDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Timber.d("deepLink = $deepLink")
                    if (deepLink != null) {
                        navController.navigate(deepLink)
                    }
                }
            }
            .addOnFailureListener(this) { e -> Timber.w("getDynamicLink:onFailure $e") }
    }
}