package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentSignNameBinding
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.model.enums.ProfileViewType
import com.depromeet.housekeeper.model.enums.SignViewType

class SignNameFragment : Fragment() {
    lateinit var binding : FragmentSignNameBinding
    private val viewmodel : SignNameViewModel by viewModels()
    private val navArgs by navArgs<SignNameFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_name,container,false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = viewmodel


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingvm()
        initListener()
        validateName()
    }

    private fun bindingvm() {
        viewmodel.setViewType(navArgs.viewType)
        binding.viewType = viewmodel.viewType.value
    }

    private fun initListener() {
        binding.signNameHeader.defaultHeaderTitleTv.text = ""
        binding.signNameHeader.defaultHeaderBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.signNameNextBtn.mainFooterButton.setText(R.string.sign_name_next_btn_text)
        binding.signNameNextBtn.mainFooterButton.setOnClickListener {
            when(viewmodel.viewType.value){
                SignViewType.UserName -> {
                    findNavController().navigate(
                        SignNameFragmentDirections.actionSignNameFragmentToSignProfileFragment(
                            name = viewmodel.inputName.value,viewType = ProfileViewType.Sign))
                }
                SignViewType.GroupName -> {
                    findNavController().navigate(SignNameFragmentDirections.actionSignNameFragmentToInviteFragment(
                        viewType = InviteViewType.SIGN
                    ))
                }
                SignViewType.InviteCode -> {
                    findNavController().navigate(R.id.action_signNameFragment_to_mainFragment)
                }
            }
        }
        binding.signNameClear.setOnClickListener {
            binding.signNameEt.setText(R.string.sign_name_blank)
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
                viewmodel.setInputName(binding.signNameEt.text.toString())
                binding.isTextChanged = true
                if (!value.matches(pattern.toRegex())) {
                    binding.isError = true
                    binding.signNameNextBtn.mainFooterButton.isEnabled = false
                } else {
                    binding.isError = false
                    binding.signNameNextBtn.mainFooterButton.isEnabled =
                        viewmodel.inputName.value != ""
                }
                if (value == ""){
                    binding.isTextChanged = false
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        )
    }
}