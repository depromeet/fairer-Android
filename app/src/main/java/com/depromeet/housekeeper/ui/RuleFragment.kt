package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.adapter.RuleAdapter
import com.depromeet.housekeeper.databinding.FragmentRuleBinding
import kotlinx.coroutines.flow.collect

class RuleFragment : Fragment() {
  lateinit var binding: FragmentRuleBinding
  private val viewModel: RuleViewModel by viewModels()
  lateinit var adapter: RuleAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rule, container, false)
    binding.lifecycleOwner = this.viewLifecycleOwner
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initListener()
    setAdapter()
    bindingVm()
  }

  private fun bindingVm() {
    lifecycleScope.launchWhenStarted {
      viewModel.rules.collect {
        when {
          it.count() > 0 -> {
            binding.tvRule.visibility = View.VISIBLE
            binding.rvRules.visibility = View.VISIBLE
            adapter.updateDate(it.toMutableList())
          }
          else -> {
            binding.tvRule.visibility = View.GONE
            binding.rvRules.visibility = View.GONE
          }
        }
      }
    }

  }

  private fun setAdapter() {
    adapter = RuleAdapter(viewModel.rules.value.toMutableList()) { viewModel.deleteRule(it) }
    binding.rvRules.adapter = adapter
  }

  private fun initListener() {

    binding.ruleHeader.apply {
      defaultHeaderBackBtn.setOnClickListener {
        it.findNavController().navigateUp()
      }
    }

    binding.etRule.setOnKeyListener { v, keyCode, event ->
      if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
        val ruleName = binding.etRule.text
        if (ruleName.isNotEmpty()) {
          viewModel.createRule(ruleName.toString())
          binding.etRule.text.clear()
          binding.etRule.requestFocus()
        }
      }
      true
    }
  }
}