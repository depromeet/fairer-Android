package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentSignNameBinding
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.model.enums.ProfileViewType
import com.depromeet.housekeeper.model.enums.SignViewType
import kotlinx.coroutines.flow.collect

class SignNameFragment : Fragment() {
    lateinit var binding: FragmentSignNameBinding
    private val viewModel: SignNameViewModel by viewModels()
    private val navArgs by navArgs<SignNameFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_name, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = viewModel


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingVm()
        initListener()
        validateName()
    }

    private fun bindingVm() {
        viewModel.setViewType(navArgs.viewType)
        binding.viewType = viewModel.viewType.value
        lifecycleScope.launchWhenCreated {
            viewModel.hasTeam.collect {
                binding.hasTeam = it
                binding.hasTeamLayout.failedGroupNextBtn.mainFooterButton.text = getString(R.string.failed_group_button_text)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.responseJoinTeam.collect {
                if (it != null) {
                    findNavController().navigate(SignNameFragmentDirections.actionSignNameFragmentToGroupInfoFragment())
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.responseTeamUpdate.collect {
                if (it != null) {
                    findNavController().navigateUp()
                    Toast.makeText(context, R.string.modify_group_toast_massage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.errorMessage.collect {
                if (it.isNotEmpty()){
                    Toast.makeText(this@SignNameFragment.context,it,Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    private fun initListener() {
        binding.signNameHeader.defaultHeaderTitleTv.text = ""
        binding.signNameHeader.defaultHeaderBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.signNameNextBtn.mainFooterButton.setText(R.string.sign_name_next_btn_text)
        binding.signNameNextBtn.mainFooterButton.setOnClickListener {
            when (viewModel.viewType.value) {
                SignViewType.UserName -> {
                    findNavController().navigate(
                        SignNameFragmentDirections.actionSignNameFragmentToSignProfileFragment(
                            name = viewModel.inputText.value, viewType = ProfileViewType.Sign
                        )
                    )
                }
                SignViewType.GroupName -> {
                    findNavController().navigate(
                        SignNameFragmentDirections.actionSignNameFragmentToInviteFragment(
                            houseName = viewModel.inputText.value, viewType = InviteViewType.SIGN
                        )
                    )
                }
                SignViewType.InviteCode -> {
                    viewModel.joinTeam(viewModel.inputText.value)
                }
                SignViewType.ModifyGroupName -> {
                    viewModel.teamNameUpdate(viewModel.inputText.value)
                }
            }
        }
        binding.signNameBackground.setOnClickListener{
            binding.signNameEt.isEnabled = false
            binding.isTextChanged = false
            binding.signNameEt.isEnabled = true
        }
        binding.signNameClear.setOnClickListener {
            binding.signNameEt.setText(R.string.sign_name_blank)
        }
        if (navArgs.code != null) {
            if (navArgs.code == "hasTeam") {
                viewModel.setHasTeam(true)
                binding.hasTeamLayout.failedGroupNextBtn.mainFooterButton.isEnabled = true

            }
            viewModel.setInputText(navArgs.code!!)
            binding.signNameEt.setText(navArgs.code)
            binding.signNameNextBtn.mainFooterButton.isEnabled = viewModel.inputText.value != ""

        }
        binding.hasTeamLayout.failedGroupNextBtn.mainFooterButton.setOnClickListener {
            findNavController().navigate(SignNameFragmentDirections.actionSignNameFragmentToMainFragment())
        }

        binding.signNameEt.setOnTouchListener { status, _ ->
            status.requestFocus()
            if(viewModel.inputText.value.isNotEmpty()){
                binding.isTextChanged=true
            }
            false
        }
    }

    private fun validateName() {
        val pattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|ㆍᆢ| ]*"
        binding.signNameEt.addTextChangedListener{
            val value: String = binding.signNameEt.text.toString()
            viewModel.setInputText(binding.signNameEt.text.toString())
            binding.isTextChanged = true
            if (!value.matches(pattern.toRegex())) {
                binding.isError = true
                binding.signNameNextBtn.mainFooterButton.isEnabled = false
            } else {
                binding.isError = false
                binding.signNameNextBtn.mainFooterButton.isEnabled =
                    viewModel.inputText.value != ""
            }
            if (value == "") {
                binding.isTextChanged = false
            }
        }
    }
}

