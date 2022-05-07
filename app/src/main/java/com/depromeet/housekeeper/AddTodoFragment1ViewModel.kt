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

  private val _chorelist: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
  val chorelist: StateFlow<List<String>>
  get() = _chorelist

  private val _selectSpace: MutableStateFlow<String> = MutableStateFlow("")
  val selectSpace: StateFlow<String>
    get() = _selectSpace

  private val _chores: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
  val chores: StateFlow<List<String>>
    get() = _chores

  fun setChoreList(space:String){
    when(space){
      "ENTRANCE"->{
        _chorelist.value = _choreListEntrance.value?.houseWorks ?: emptyList()
      }
      "LIVINGROOM"->{
        _chorelist.value = _choreListLivingRoom.value?.houseWorks ?: emptyList()
      }
      "BATHROOM"->{
        _chorelist.value = _choreListBathroom.value?.houseWorks ?: emptyList()
      }
      "OUTSIDE"->{
        _chorelist.value = _choreListOutside.value?.houseWorks ?: emptyList()
      }
      "ROOM"->{
        _chorelist.value = _choreListRoom.value?.houseWorks ?: emptyList()
      }
      "KITCHEN"->{
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
      Repository.getHouseWorkList("ENTRANCE").collect {
        _choreListEntrance.value = it
      }
      Repository.getHouseWorkList("LIVINGROOM").collect {
        _choreListLivingRoom.value = it
      }
      Repository.getHouseWorkList("BATHROOM").collect {
        _choreListBathroom.value = it
      }
      Repository.getHouseWorkList("OUTSIDE").collect {
        _choreListOutside.value = it
      }
      Repository.getHouseWorkList("ROOM").collect {
        _choreListRoom.value = it
      }
      Repository.getHouseWorkList("KITCHEN").collect {
        _choreListKitchen.value = it
      }
    }
  }
}