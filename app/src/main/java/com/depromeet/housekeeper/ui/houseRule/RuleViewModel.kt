package com.depromeet.housekeeper.ui.houseRule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.model.request.Rule
import com.depromeet.housekeeper.model.response.RuleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RuleViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {
    init {
        getRules()
    }

    private val _ruleName = MutableStateFlow<String>("")
    val ruleName: StateFlow<String>
        get() = _ruleName

    private val _rules = MutableStateFlow<List<RuleResponse>>(emptyList())
    val rules: StateFlow<List<RuleResponse>>
        get() = _rules

    private val _backgroundBox = MutableStateFlow<Int>(0)
    val backgroundBox get() = _backgroundBox

    fun setBackgroundBox(type: Int) {
        _backgroundBox.value = type
    }


    /**
     * Network Communication
     */
    fun createRule(ruleName: String) {
        viewModelScope.launch {
            mainRepository.createRule(Rule(ruleName))
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result != null) getRules()
                }
        }
    }

    private fun getRules() {
        viewModelScope.launch {
            mainRepository.getRules()
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result != null) {
                        _rules.value = result.ruleResponseDtos.sortedByDescending { it.ruleId }
                    }
                }
        }
    }

    fun deleteRule(ruleId: Int) {
        viewModelScope.launch {
            mainRepository.deleteRule(ruleId)
                .collect {
                    val result = receiveApiResult(it)
                    if (result != null && result.code == 200) {
                        getRules()
                    }
                }
        }
    }

}