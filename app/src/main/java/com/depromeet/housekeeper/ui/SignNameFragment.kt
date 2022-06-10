package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentSignNameBinding

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
        initListener()
        bindingvm()
        validateName()
    }

    private fun bindingvm() {
        viewmodel.setViewType(navArgs.viewType)
        binding.viewType = viewmodel.viewType.value
    }

    private fun initListener() {
        viewmodel.setViewType(navArgs.viewType)
        binding.signNameNextBtn.mainFooterButton.setText(R.string.sign_name_next_btn_text)
        binding.signNameNextBtn.mainFooterButton.setOnClickListener {
            findNavController().navigate(
                SignNameFragmentDirections.actionSignNameFragmentToSignProfileFragment(
                    viewmodel.inputName.value
                )
            )
        }
    }

    private fun validateName() {
        binding.signNameEt.addTextChangedListener {
            viewmodel.setInputName(binding.signNameEt.text.toString())
            val value: String = binding.signNameEt.text.toString()
            val emailPattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*"
            if (!value.matches(emailPattern.toRegex())) {
                binding.isError = true
                binding.signNameNextBtn.mainFooterButton.isEnabled = false
            } else {
                binding.isError = false
                binding.signNameNextBtn.mainFooterButton.isEnabled = viewmodel.inputName.value!=""
            }
        }
    }

}