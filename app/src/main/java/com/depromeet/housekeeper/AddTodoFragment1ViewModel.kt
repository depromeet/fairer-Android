package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddTodoFragment1ViewModel : ViewModel() {

  private val _selectSpace: MutableStateFlow<String> = MutableStateFlow("")
  val selectSpace: StateFlow<String>
    get() = _selectSpace

  private val _chores: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
  val chores: StateFlow<List<String>>
    get() = _chores

  fun setSpace(space: String) {
    _selectSpace.value = space
  }

  fun getChoreCount():Int{
    return _chores.value.size
  }

  fun updateChores(chores: String, isSelect: Boolean) {
    when {
      isSelect -> if (!_chores.value.contains(chores)) {
        _chores.value = _chores.value.plus(chores)
      }
      else -> if (_chores.value.contains(chores)) {
        _chores.value = _chores.value.minus(chores)
      }
    }
  }
}