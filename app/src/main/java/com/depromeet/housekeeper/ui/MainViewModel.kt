package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class MainViewModel : ViewModel() {
  private val calendar = Calendar.getInstance()

  private val _currentDate: MutableStateFlow<String> =
    MutableStateFlow("${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월")
  val currentDate: String
    get() = _currentDate.value

  private val _completeChoreNum: MutableStateFlow<Int> =
    MutableStateFlow(17)
  val completeChoreNum: StateFlow<Int>
    get() = _completeChoreNum
}