package com.depromeet.housekeeper.ui.signIn

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentSignNameBinding
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.model.enums.ProfileViewType
import com.depromeet.housekeeper.model.enums.SignViewType
import com.depromeet.housekeeper.util.EditTextUtil
import com.depromeet.housekeeper.util.EditTextUtil.hideKeyboard
import com.depromeet.housekeeper.util.EditTextUtil.showKeyboard
import com.depromeet.housekeeper.util.NavigationUtil.navigateSafe
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignNameFragment : BaseFragment<FragmentSignNameBinding>(R.layout.fragment_sign_name) {
    private val viewModel: SignNameViewModel by viewModels()
    private val navArgs by navArgs<SignNameFragmentArgs>()

    override fun createView(binding: FragmentSignNameBinding) {
        binding.vm = viewModel
    }

    override fun viewCreated() {
        binding.etText = ""
        initView()
        bindingVm()
        initListener()
        validateInput(viewModel.viewType.value)
    }

    private fun initView(){
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun bindingVm() {
        viewModel.setViewType(navArgs.viewType)
        if(viewModel.viewType.value==SignViewType.ModifyGroupName){
            viewModel.getMyTeam()
        }
        binding.viewType = viewModel.viewType.value
        binding.signNameEt.apply {
            hint = when (viewModel.viewType.value) {
                SignViewType.UserName -> getString(R.string.sign_name_hint)
                SignViewType.InviteCode -> getString(R.string.invite_code_hint)
                else -> getString(R.string.group_name_hint)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.hasTeam.collect {
                binding.hasTeam = it
                binding.hasTeamLayout.failedGroupNextBtn.mainFooterButton.text =
                    getString(R.string.failed_group_button_text)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.responseMyTeam.collect {
                if (it != null) {
                    if(viewModel.viewType.value==SignViewType.ModifyGroupName){
                        binding.signNameEt.hint = it.teamName
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.responseJoinTeam.collect {
                if (it != null) {
                    viewModel.setIsNextBtnClickable(true)
                    if (viewModel.isDynamicLink.value) {
                        findNavController().navigateSafe(R.id.action_signNameFragment_to_groupInfoFragment2)
                    } else {
                        findNavController().navigateSafe(R.id.action_signNameFragment_to_groupInfoFragment)
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.responseTeamUpdate.collect {
                viewModel.setIsNextBtnClickable(true)
                if (it != null) {
                    findNavController().navigateUp()
                    Toast.makeText(context, R.string.modify_group_toast_massage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.errorMessage.collect {
                if (it.isNotEmpty()) {
                    Toast.makeText(this@SignNameFragment.context, it, Toast.LENGTH_LONG).show()
                    viewModel.setErrorMessage()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect {
                binding.layoutNetwork.isNetworkError = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.backgroundBox.collect {
                when (it) {
                    1 -> { // edit 중
                        binding.clEt.background =
                            resources.getDrawable(R.drawable.fairer_edit_text_focus_background)
                    }

                    2 -> { // error
                        binding.clEt.background =
                            resources.getDrawable(R.drawable.edit_text_error_background)
                    }

                    else -> { // default
                        binding.clEt.background =
                            resources.getDrawable(R.drawable.sign_name_edit_text_background)
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        binding.signNameHeader.defaultHeaderTitleTv.text = ""
        binding.signNameHeader.defaultHeaderBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.signNameNextBtn.mainFooterButton.setText(R.string.sign_name_next_btn_text)
        binding.signNameNextBtn.mainFooterButton.setOnClickListener {
            if (!viewModel.isNextBtnClickable) return@setOnClickListener
            when (viewModel.viewType.value) {
                SignViewType.UserName -> {
                    viewModel.setMemberName(binding.etText!!)
                    findNavController().navigate(
                        SignNameFragmentDirections.actionSignNameFragmentToSignProfileFragment(
                            name = binding.etText, viewType = ProfileViewType.Sign
                        )
                    )
                }

                SignViewType.GroupName -> {
                    findNavController().navigate(
                        SignNameFragmentDirections.actionSignNameFragmentToInviteFragment(
                            houseName = binding.etText, viewType = InviteViewType.SIGN
                        )
                    )
                }

                SignViewType.InviteCode -> {
                    viewModel.joinTeam(binding.etText!!)
                    viewModel.setIsNextBtnClickable(false)
                }

                SignViewType.ModifyGroupName -> {
                    viewModel.teamNameUpdate(binding.etText!!)
                    viewModel.setIsNextBtnClickable(false)
                }
            }
        }
        binding.signNameClear.setOnClickListener {
            binding.signNameEt.setText(R.string.sign_name_blank)
        }
        if (navArgs.code != null) {
            if (navArgs.code == "hasTeam") {
                viewModel.setHasTeam(true)
                binding.hasTeamLayout.failedGroupNextBtn.mainFooterButton.isEnabled = true

            }
            binding.etText = (navArgs.code!!)
            binding.signNameEt.setText(navArgs.code)
            binding.signNameNextBtn.mainFooterButton.isEnabled = binding.etText != ""
            viewModel.setDynamicLink(true)

        }
        binding.hasTeamLayout.failedGroupNextBtn.mainFooterButton.setOnClickListener {
            findNavController().navigate(SignNameFragmentDirections.actionSignNameFragmentToMainFragment())
        }

        binding.signNameEt.apply {
            setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    binding.isTextChanged = false
                    viewModel.setBackgroundBox(0)
                    hideKeyboard(requireContext(), this)
                } else {
                    if (binding.etText!!.isNotEmpty()) binding.isTextChanged = true
                    viewModel.setBackgroundBox(1)
                    showKeyboard(requireContext(), this)
                }
            }
        }
    }
    private fun validateInput(signViewType: SignViewType) {
        binding.signNameEt.addTextChangedListener {
            binding.etText = it.toString()
            binding.isTextChanged = true

            when (signViewType) {
                SignViewType.UserName -> {
                    if (!it.toString().matches(EditTextUtil.INPUT_PATTERN.toRegex())) {
                        binding.isError = true
                        binding.signNameNextBtn.mainFooterButton.isEnabled = false
                        binding.signNameError.setText(R.string.sign_name_error)
                        viewModel.setBackgroundBox(2)
                    }
                    else if (it.toString().length > 5) {
                        binding.isError = true
                        binding.signNameNextBtn.mainFooterButton.isEnabled = false
                        binding.signNameError.setText(R.string.sign_name_text_over_error)
                        viewModel.setBackgroundBox(2)
                    } else {
                        binding.isError = false
                        binding.signNameNextBtn.mainFooterButton.isEnabled =
                            binding.etText != ""
                        viewModel.setBackgroundBox(1)

                    }
                }
                SignViewType.InviteCode -> {
                    if (!it.toString().matches(EditTextUtil.INPUT_PATTERN.toRegex())) {
                        binding.isError = true
                        binding.signNameNextBtn.mainFooterButton.isEnabled = false
                        binding.signNameError.setText(R.string.sign_name_error)
                        viewModel.setBackgroundBox(2)
                    }
                    else if (it.toString().length > 12) {
                        binding.isError = true
                        binding.signNameNextBtn.mainFooterButton.isEnabled = false
                        binding.signNameError.setText(R.string.sign_invite_text_over_error)
                        viewModel.setBackgroundBox(2)
                    } else {
                        binding.isError = false
                        binding.signNameNextBtn.mainFooterButton.isEnabled = binding.etText != ""
                        viewModel.setBackgroundBox(1)
                    }
                }
                SignViewType.GroupName, SignViewType.ModifyGroupName -> {
                    if (!it.toString().matches(EditTextUtil.INPUT_PATTERN.toRegex())) {
                        binding.isError = true
                        binding.signNameNextBtn.mainFooterButton.isEnabled = false
                        binding.signNameError.setText(R.string.sign_name_error)
                        viewModel.setBackgroundBox(2)
                    }
                    else if (it.toString().length > 16) {
                        binding.isError = true
                        binding.signNameNextBtn.mainFooterButton.isEnabled = false
                        binding.signNameError.setText(R.string.sign_group_text_over_error)
                        viewModel.setBackgroundBox(2)
                    } else {
                        binding.isError = false
                        binding.signNameNextBtn.mainFooterButton.isEnabled = binding.etText != ""
                        viewModel.setBackgroundBox(1)
                    }
                }
            }

            if (it.toString().isEmpty()) {
                binding.isTextChanged = false
            }
        }
    }


}

