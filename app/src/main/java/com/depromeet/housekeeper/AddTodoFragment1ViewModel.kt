package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.adapter.AddTodo1ChoreAdapter
import com.depromeet.housekeeper.model.ChoreList
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class AddTodoFragment1ViewModel : ViewModel() {
  init {
    getChoreList()
  }
  private val _chorelist: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
  val chorelist:StateFlow<List<String>>
  get() = _chorelist

  init {
    getChoreList()
  }

  private val _chorelist: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
  val chorelist:StateFlow<List<String>>
  get() = _chorelist

  private val _selectSpace: MutableStateFlow<String> = MutableStateFlow("")
  val selectSpace: StateFlow<String>
    get() = _selectSpace

  private val _chores: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
  val chores: StateFlow<List<String>>
    get() = _chores

  fun setChoreList(space:String){
    when(space){
      "entrance"->{
        _chorelist.value = _choreListEntrance.value?.houseWorks ?: emptyList()
      }
      "livingroom"->{
        _chorelist.value = _choreListLivingRoom.value?.houseWorks ?: emptyList()
      }
      "bathroom"->{
        _chorelist.value = _choreListBathroom.value?.houseWorks ?: emptyList()
      }
      "outside"->{
        _chorelist.value = _choreListOutside.value?.houseWorks ?: emptyList()
      }
      "room"->{
        _chorelist.value = _choreListRoom.value?.houseWorks ?: emptyList()
      }
      "kitchen"->{
        _chorelist.value = _choreListKitchen.value?.houseWorks ?: emptyList()
      }
    }
  }

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


  private val _choreListEntrance: MutableStateFlow<ChoreList?> = MutableStateFlow(null)
  val choreListEntrance : StateFlow<ChoreList?>
    get()=_choreListEntrance


  private val _choreListLivingRoom: MutableStateFlow<ChoreList?> = MutableStateFlow(null)
  val choreListLivingRoom : StateFlow<ChoreList?>
    get()=_choreListLivingRoom

  private val _choreListBathroom: MutableStateFlow<ChoreList?> = MutableStateFlow(null)
  val choreListBathroom : StateFlow<ChoreList?>
    get()=_choreListBathroom

  private val _choreListOutside: MutableStateFlow<ChoreList?> = MutableStateFlow(null)
  val choreListOutside : StateFlow<ChoreList?>
    get()=_choreListOutside

  private val _choreListRoom: MutableStateFlow<ChoreList?> = MutableStateFlow(null)
  val choreListRoom : StateFlow<ChoreList?>
    get()=_choreListRoom

  private val _choreListKitchen: MutableStateFlow<ChoreList?> = MutableStateFlow(null)
  val choreListKitchen : StateFlow<ChoreList?>
    get()=_choreListKitchen

  private fun getChoreList(){
    viewModelScope.launch {
      Repository.getHouseWorkList("entrance").collect {
        _choreListEntrance.value = it
      }
      Repository.getHouseWorkList("livingroom").collect {
        _choreListLivingRoom.value = it
      }
      Repository.getHouseWorkList("bathroom").collect {
        _choreListBathroom.value = it
      }
      Repository.getHouseWorkList("outside").collect {
        _choreListOutside.value = it
      }
      Repository.getHouseWorkList("room").collect {
        _choreListRoom.value = it
      }
      Repository.getHouseWorkList("kitchen").collect {
        _choreListKitchen.value = it
      }
    }
  }
}