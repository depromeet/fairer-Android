package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.ProfileData
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SettingProfileViewModel : ViewModel() {

  init {
    getMe()
  }

  private val _myData: MutableStateFlow<ProfileData?> = MutableStateFlow(null)
  val myData: StateFlow<ProfileData?>
    get() = _myData


  private fun getMe() {
    viewModelScope.launch {
      Repository.getMe().runCatching {
        collect {
          _myData.value = it
        }
      }
    }
  }

  fun updateMe(
    memberName: String,
    profilePath: String,
    statueMessage: String,
  ) {
    viewModelScope.launch {
      Repository.updateMe(memberName, profilePath, statueMessage)
        .runCatching {
          collect {
            it.statusMessage
          }
        }
    }
  }
}