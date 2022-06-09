package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentSignNameBinding
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SignNameFragment : Fragment() {
    lateinit var binding : FragmentSignNameBinding
    private val viewmodel : SignNameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_name,container,false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = viewmodel


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        bindingvm()
    }

    private fun bindingvm() {
        lifecycleScope.launchWhenCreated {
            viewmodel.inputName.collect {
                binding.signNameNextBtn.mainFooterButton.isEnabled = viewmodel.inputName.value!=""
                Timber.d(binding.signNameEt.text.toString())
            }
        }
    }

    private fun initListener(){
        binding.signNameNextBtn.mainFooterButton.setText(R.string.sign_name_next_btn_text)
        binding.signNameEt.addTextChangedListener {
            viewmodel.setinputname(binding.signNameEt.text.toString())
        }
        binding.signNameNextBtn.mainFooterButton.setOnClickListener {
            findNavController().navigate(SignNameFragmentDirections.actionSignNameFragmentToSignProfileFragment(viewmodel.inputName.value))
        }
    }
}