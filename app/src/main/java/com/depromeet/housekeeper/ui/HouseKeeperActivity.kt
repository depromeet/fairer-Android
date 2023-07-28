package com.depromeet.housekeeper.ui

import android.content.*
import android.content.Intent.ACTION_SEND
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ActivityHousekeeperBinding
import com.depromeet.housekeeper.service.InternetService
import com.depromeet.housekeeper.ui.signIn.LoginFragment
import com.depromeet.housekeeper.util.FILTER_INTERNET_CONNECTED
import com.depromeet.housekeeper.util.IS_INTERNET_CONNECTED
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HouseKeeperActivity : AppCompatActivity() {
    private val tokenViewModel: TokenViewModel by viewModels()
    private val viewModel: HouseKeeperViewModel by viewModels()
    lateinit var binding: ActivityHousekeeperBinding
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    private lateinit var mService: InternetService
    private var mBound = false

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as InternetService.InternetBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }

    }

    private val internetReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, mIntent: Intent?) {
            if (mIntent != null && mIntent.action == ACTION_SEND) {
                val isInternetConnected = mIntent.getBooleanExtra(IS_INTERNET_CONNECTED, false)
                viewModel.setIsInternetDisconnected(!isInternetConnected)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoading.value }
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_housekeeper)
        binding.lifecycleOwner = this
        bindingVm()
        getDynamicLink()
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, InternetService::class.java), conn, Context.BIND_AUTO_CREATE)
        val filter = IntentFilter(FILTER_INTERNET_CONNECTED).apply {
            addAction(ACTION_SEND)
        }
        registerReceiver(internetReceiver, filter)

        tokenViewModel.refreshToken.observe(this) {
            if (it == null && GoogleSignIn.getLastSignedInAccount(applicationContext) != null) {
                Timber.d("auth refresh token is null")
                LoginFragment.mGoogleSignInClient.signOut().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d("로그아웃 성공적 ${GoogleSignIn.getLastSignedInAccount(applicationContext)}")
                        while (navHostFragment.parentFragmentManager.backStackEntryCount > 0) navController.popBackStack()
                        val navOption =
                            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                        navController.navigate(R.id.loginFragment, null, navOptions = navOption)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(conn)
            mBound = false
        }
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenStarted {
            viewModel.isInternetDisconnected.collect {
                Timber.d(">>> bindingVm : isInternetDisconnected = $it")
                binding.layoutInternetDisconnected.root.bringToFront()
                binding.layoutInternetDisconnected.root.visibility =
                    if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                if (pendingDynamicLinkData != null) {
                    val deepLink: Uri? = pendingDynamicLinkData.link
                    Timber.d("deepLink = $deepLink")
                    if (deepLink != null) {
                        navController.navigate(deepLink)
                    }
                }
            }
            .addOnFailureListener(this) { e -> Timber.w("getDynamicLink:onFailure $e") }
    }
}