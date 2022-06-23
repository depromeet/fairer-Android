package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.Rule
import com.depromeet.housekeeper.model.RuleResponse
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RuleViewModel : ViewModel() {
  init {
    getRules()
  }

  private val _ruleName = MutableStateFlow<String>("")
  val ruleName: StateFlow<String>
    get() = _ruleName

  private val _rules = MutableStateFlow<List<RuleResponse>>(emptyList())
  val rules: StateFlow<List<RuleResponse>>
    get() = _rules

  fun createRule(ruleName: String) {
    viewModelScope.launch {
      Repository.createRule(Rule(ruleName))
        .runCatching {
          collect {
            getRules()
          }
        }
    }
  }

  private fun getRules() {
    viewModelScope.launch {
      Repository.getRules()
        .runCatching {
          collect {
            _rules.value = it.ruleResponseDtos
          }
        }
    }
  }

  fun deleteRule(ruleId: Int) {
    viewModelScope.launch {
      Repository.deleteRule(ruleId)
        .runCatching {
          collect {
            if (it.code == 200) {
              getRules()
            }
          }
        }
    }
  }

}