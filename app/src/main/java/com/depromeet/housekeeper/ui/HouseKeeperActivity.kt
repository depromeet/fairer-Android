package com.depromeet.housekeeper.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ActivityMainBinding
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class HouseKeeperActivity : AppCompatActivity() {

  private val viewModel : HouseKeeperViewModel by viewModels()
  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen().apply {
      setKeepOnScreenCondition{viewModel.isLoading.value}
    }
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.lifecycleOwner = this
    getDynamicLink()
  }

  private fun getDynamicLink() {
    Firebase.dynamicLinks
      .getDynamicLink(intent)
      .addOnSuccessListener(this) { pendingDynamicLinkData ->
        // Get deep link from result (may be null if no link is found)
        var deepLink: Uri? = null
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        if (pendingDynamicLinkData != null) {
          deepLink = pendingDynamicLinkData.link
          Timber.d("deepLink = $deepLink")
          if (deepLink != null) {
            navController.navigate(deepLink)
          }
        }
      }
      .addOnFailureListener(this) { e -> Timber.w( "getDynamicLink:onFailure $e") }
  }
}