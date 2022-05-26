package com.depromeet.housekeeper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.depromeet.housekeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.lifecycleOwner = this
  }

}