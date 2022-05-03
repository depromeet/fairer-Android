package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import com.depromeet.housekeeper.model.Chore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.collections.ArrayList

class AddTodo2ViewModel: ViewModel(){
    private val _currentChores = MutableStateFlow<ArrayList<Chore>>(arrayListOf(Chore(), Chore(), Chore(), Chore()))
    val currentChores: ArrayList<Chore>
        get() = _currentChores.value

    fun updateValue(chores: ArrayList<Chore>) {
        _currentChores.value = chores
    }
}