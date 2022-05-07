package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.ChoreList
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddTodoFragment1ViewModel : ViewModel() {
  init {
    getChoreList()
  }
  private val _chorepreset: MutableStateFlow<List<ChoreList>> = MutableStateFlow(listOf())
  val chorepreset: StateFlow <List<ChoreList>>
    get() = _chorepreset

  private val _chorelist: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
  val chorelist: StateFlow<List<String>>
    get() = _chorelist

  private val _selectSpace: MutableStateFlow<String> = MutableStateFlow("")
  val selectSpace: StateFlow<String>
    get() = _selectSpace

  private val _chores: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
  val chores: StateFlow<List<String>>
    get() = _chores

  fun setChoreList(space: String) {
    for (i in 0 until _chorepreset.value.size){
      if(space == _chorepreset.value[i].space){
        _chorelist.value = _chorepreset.value[i].houseWorks
      }
    }
  }


  fun setSpace(space: String) {
    _selectSpace.value = space
  }

  fun getChoreCount(): Int {
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

  private fun getChoreList() {
    viewModelScope.launch {
      Repository.getHouseWorkList().collect {
        _chorepreset.value = it.preset
      }
    }
  }
}