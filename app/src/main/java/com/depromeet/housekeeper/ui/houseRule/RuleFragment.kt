package com.depromeet.housekeeper.ui.houseRule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentRuleBinding
import com.depromeet.housekeeper.ui.houseRule.adapter.RuleAdapter
import com.depromeet.housekeeper.util.EditTextUtil.hideKeyboard
import com.depromeet.housekeeper.util.EditTextUtil.listenEditorDoneAction
import com.depromeet.housekeeper.util.EditTextUtil.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RuleFragment : BaseFragment<FragmentRuleBinding>(R.layout.fragment_rule) {
    private val viewModel: RuleViewModel by viewModels()
    lateinit var adapter: RuleAdapter

    override fun createView(binding: FragmentRuleBinding) {
        initView()
    }

    override fun viewCreated() {
        initListener()
        setAdapter()
        bindingVm()
    }


    private fun initView() {
        binding.textRule = ""
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenStarted {
            viewModel.rules.collect {
                when {
                    it.isNotEmpty() -> {
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
        lifecycleScope.launchWhenCreated {
            viewModel.backgroundBox.collect {
                when (it) {
                    1 -> { // edit 중
                        binding.clRule.background =
                            resources.getDrawable(R.drawable.fairer_edit_text_focus_background)
                        binding.ivInfo.setColorFilter(requireContext().getColor(R.color.gray_200))
                        binding.tvInfo.setTextColor(resources.getColor(R.color.gray_600))
                    }
                    2 -> { // error
                        binding.clRule.background =
                            resources.getDrawable(R.drawable.edit_text_error_background)
                        binding.ivInfo.setColorFilter(requireContext().getColor(R.color.negative_20))
                        binding.tvInfo.setTextColor(resources.getColor(R.color.negative_20))
                    }
                    else -> { // default
                        binding.clRule.background =
                            resources.getDrawable(R.drawable.sign_name_edit_text_background)
                        binding.ivInfo.setColorFilter(requireContext().getColor(R.color.gray_200))
                        binding.tvInfo.setTextColor(resources.getColor(R.color.gray_600))
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
        binding.btnEtClear.setOnClickListener {
            binding.textRule = ""
        }

        binding.ruleHeader.apply {
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.etRule.apply {
            doOnTextChanged { text, start, before, count ->
                binding.textRule = text.toString()
                binding.isTextChanged = count != 0

                if (adapter.itemCount >= 10) {
                    binding.textRule = ""
                    viewModel.setBackgroundBox(2)
                    Toast.makeText(requireContext(), R.string.rule_info, Toast.LENGTH_LONG).show()
                } else {
                    viewModel.setBackgroundBox(1)
                }
            }

            listenEditorDoneAction {
                viewModel.createRule(it)
                hideKeyboard(requireContext(), this)
            }

            setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    binding.isTextChanged = false
                    viewModel.setBackgroundBox(0)
                    hideKeyboard(requireContext(), this)
                } else {
                    if (binding.textRule != "") binding.isTextChanged = true
                    viewModel.setBackgroundBox(1)
                    showKeyboard(requireContext(), this)
                }
            }
        }
    }


}