package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.response.collect {
                findNavController().navigateUp()
                Toast.makeText(context,R.string.modify_group_toast_massage,Toast.LENGTH_SHORT).show()
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
                    findNavController().navigate(R.id.action_signNameFragment_to_groupInfoFragment)
                }
                SignViewType.ModifyGroupName ->{
                    viewModel.teamNameUpdate(viewModel.inputText.value)
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
            viewModel.setInputText(navArgs.code!!)
            binding.signNameEt.setText(navArgs.code)
            binding.signNameNextBtn.mainFooterButton.isEnabled = viewModel.inputText.value != ""

        }
        binding.hasTeamLayout.failedGroupNextBtn.mainFooterButton.setOnClickListener {
            findNavController().navigate(SignNameFragmentDirections.actionSignNameFragmentToMainFragment())
        }
    }

    private fun validateName() {
        binding.signNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.isTextChanged = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val value: String = binding.signNameEt.text.toString()
                val pattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*"
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

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        )
    }
}