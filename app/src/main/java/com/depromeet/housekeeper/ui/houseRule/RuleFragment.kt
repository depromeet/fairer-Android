package com.depromeet.housekeeper.ui.houseRule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentRuleBinding
import com.depromeet.housekeeper.ui.houseRule.adapter.RuleAdapter
import com.depromeet.housekeeper.util.EditTextUtil.hideKeyboard
import com.depromeet.housekeeper.util.EditTextUtil.listenEditorDoneAction
import com.depromeet.housekeeper.util.EditTextUtil.showKeyboard
import com.depromeet.housekeeper.util.EditTextUtil.textPattern
import timber.log.Timber


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
                    it.isNotEmpty() -> {
                        binding.tvRule.visibility = View.VISIBLE
                        binding.rvRules.visibility = View.VISIBLE
                        adapter.updateDate(it.toMutableList())
                        if (it.count() <10) {
                            binding.etRule.fairerEt.isEnabled = true
                        }
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
        binding.etRule.signNameClear.setOnClickListener {
            binding.etRule.fairerEt.setText(R.string.sign_name_blank)
        }

        binding.ruleHeader.apply {
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.etRule.clFairerEt.setOnClickListener {
            binding.etRule.fairerEt.requestFocus()
            binding.etRule.fairerEt.isFocusable = true
        }

        binding.etRule.fairerEt.apply {
            listenTextChanged()
            listenEditorDoneAction() {
                viewModel.createRule(it)
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(requireContext(), view)
                } else {
                    showKeyboard(requireContext(), this)
                    this.isFocusableInTouchMode = true
                }

                this.isCursorVisible = hasFocus
            }

        }
    }


    private fun EditText.listenTextChanged() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Timber.d("beforeTextChanged: ${p0.toString()}")
                binding.isTextChanged = p0.toString().isNotEmpty()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Timber.d("onTextChanged: ${p0.toString()}")
                binding.isError = !p0.toString().matches(textPattern.toRegex())

                if (viewModel.rules.value.count() >= 10) {
                    this@listenTextChanged.text.clear()
                    this@listenTextChanged.isFocusable = false
                    binding.isError = true
                    binding.ivInfo.setColorFilter(requireContext().getColor(R.color.negative_20))
                    Toast.makeText(requireContext(), R.string.rule_info, Toast.LENGTH_LONG).show()
                } else {
                    binding.ivInfo.setColorFilter(requireContext().getColor(R.color.gray_200))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //Timber.d("afterTextChanged: ${p0.toString()}")
                binding.isTextChanged = p0.toString().isNotEmpty()
            }

        })
    }


}