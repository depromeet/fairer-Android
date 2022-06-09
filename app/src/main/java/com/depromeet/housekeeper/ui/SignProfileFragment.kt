package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentSignProfileBinding
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SignProfileFragment : Fragment() {
    lateinit var binding : FragmentSignProfileBinding
    private val viewmodel : SignProfileViewModel by viewModels()
    private val navArgs by navArgs<SignProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_profile,container,false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = viewmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d(navArgs.name)
        bindingVm()
        initListener()
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenCreated {
            viewmodel.isSelected.collect {
                if(it){
                    binding.signNameNextBtn.mainFooterButton.isEnabled = true
                }

            }
        }


    }

    private fun initListener() {
        binding.signProfileHeader.defaultHeaderTitleTv.text = ""
        binding.signNameNextBtn.mainFooterButton.setText(R.string.sign_profile_next_btn_text)
        binding.signProfileHeader.defaultHeaderBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.signNameNextBtn.mainFooterButton.setOnClickListener {
            findNavController().navigate(SignProfileFragmentDirections.actionSignProfileFragmentToJoinGroupFragment(name = navArgs.name))
        }
    }


}