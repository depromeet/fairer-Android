package com.depromeet.housekeeper

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

class MainActivity : AppCompatActivity() {

  private val viewmodel : MainViewModel by viewModels()
  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen().apply {
      setKeepOnScreenCondition{viewmodel.isLoading.value}
    }
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.lifecycleOwner = this
  }
}