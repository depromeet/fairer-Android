package com.depromeet.housekeeper

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.depromeet.housekeeper.databinding.ActivityMainBinding
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private val viewModel : MainViewModel by viewModels()
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

  private fun getDynamicLink(){
    Firebase.dynamicLinks
      .getDynamicLink(intent)
      .addOnSuccessListener(this) { pendingDynamicLinkData ->
        // Get deep link from result (may be null if no link is found)
        var deepLink: Uri? = null
        if (pendingDynamicLinkData != null) {
          deepLink = pendingDynamicLinkData.link
          val code = deepLink?.getQueryParameter("code")
          viewModel.setInviteCode(code!!)
          Timber.d("!@$code")
        }
        else{
          Timber.d("NO HAVE DEEP LINK")
        }
        Timber.d("!!$deepLink")

      }
      .addOnFailureListener(this) { e -> Timber.w( "getDynamicLink:onFailure $e") }
  }
}